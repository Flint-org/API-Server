package com.flint.flint.club.service.comment;

import com.flint.flint.asset.dto.LogoInfoResponse;
import com.flint.flint.asset.service.AssetService;
import com.flint.flint.club.domain.comment.ClubComment;
import com.flint.flint.club.domain.main.Club;
import com.flint.flint.club.repository.comment.ClubCommentRepository;
import com.flint.flint.club.repository.main.ClubRepository;
import com.flint.flint.club.request.ClubCommentCreateRequest;
import com.flint.flint.club.response.ClubCommentsAndRepliesResponse;
import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.domain.main.UniversityCertify;
import com.flint.flint.member.repository.MemberRepository;
import com.flint.flint.member.repository.UniversityCertifyRepository;
import com.flint.flint.member.service.MemberService;
import com.flint.flint.security.auth.dto.AuthorityMemberDTO;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.flint.flint.common.spec.ResultCode.*;

@RequiredArgsConstructor
@Service
public class ClubCommentServiceImpl {
    //todo : 모임장의 댓글의 경우 모임장 표시
    private final MemberService memberService;
    private final ClubRepository clubRepository;
    private final ClubCommentRepository clubCommentRepository;
    private final MemberRepository memberRepository;
    private final UniversityCertifyRepository universityCertifyRepository;
    private final AssetService assetService;

    private static final String COMMENT_PATTERN = "^(?=.*[ㄱ-ㅎ가-힣a-zA-Z0-9!@#$%^&*()-_+=<>?]).{300}$"; // 한글, 영문, 숫자, 특수문자, 이모티콘, 공백 포함 300자
    private static final Pattern COMMENT_REGEX = Pattern.compile(COMMENT_PATTERN);

    @Transactional
    public void createComment(AuthorityMemberDTO memberDTO,
                              ClubCommentCreateRequest request,
                              long clubId) {
        // checkIsValidComment(request.getContents());
        ClubComment clubComment = buildCommonComment(memberDTO, request, clubId);
        clubComment.setCommentParent(null); // null 일 경우 최상위 댓글
        clubCommentRepository.save(clubComment);
    }

    @Transactional
    public void createCommentReply(AuthorityMemberDTO memberDTO,
                                   @RequestBody ClubCommentCreateRequest request,
                                   @PathVariable("id") long clubId,
                                   @RequestParam long parentCommentId) {
        ClubComment parentComment = clubCommentRepository.findById(parentCommentId).orElseThrow(
                () -> new FlintCustomException(HttpStatus.NOT_FOUND, CLUB_COMMENT_NOT_FOUND_ERROR)
        );

        ClubComment createdCommentReply = buildCommonComment(memberDTO, request, clubId);
        createdCommentReply.setCommentParent(parentComment);
        parentComment.addCommentChildren(createdCommentReply);
    }

    private ClubComment buildCommonComment(AuthorityMemberDTO memberDTO, ClubCommentCreateRequest request, long clubId) {
        return ClubComment.builder()
                .contents(request.getContents())
                .club(
                        clubRepository.findById(clubId).orElseThrow(
                                () -> new FlintCustomException(HttpStatus.NOT_FOUND, CLUB_NOT_FOUND_ERROR)
                        )
                )
                .member(memberService.getMember(memberDTO.getId()))
                .build();
    }

    private static void checkIsValidComment(String contexts) {
        if (!COMMENT_REGEX.matcher(contexts).matches()) {
            throw new FlintCustomException(HttpStatus.BAD_REQUEST, CLUB_COMMENT_PATTERN_NOT_MATCH);
        }
    }

    //todo : [댓글 조회] 댓글은 시간순으로 정렬되며, 6개 이상일때, 앞에 5개의 댓글이 먼저 노출되고 댓글 더 보기 클릭 시 나머지 댓글들이 노출됩니다.
    @Transactional(readOnly = true)
    public List<ClubCommentsAndRepliesResponse> getComments(Long clubId) {
        List<ClubCommentsAndRepliesResponse> response = new ArrayList<>();

        Club club = clubRepository.findById(clubId).orElseThrow(
                () -> new FlintCustomException(HttpStatus.NOT_FOUND, CLUB_NOT_FOUND_ERROR)
        );

        List<ClubComment> clubComments = clubCommentRepository.findAllByCommentParent(club).orElseThrow(
                () -> new FlintCustomException(HttpStatus.NOT_FOUND, CLUB_COMMENT_NOT_FOUND_ERROR)
        );

        for (ClubComment comment : clubComments) {
            ClubCommentsAndRepliesResponse commentResponse = new ClubCommentsAndRepliesResponse();
            response.add(addCommentsAndReplies(comment, commentResponse));
        }

        return response;
    }

    private ClubCommentsAndRepliesResponse addCommentsAndReplies(ClubComment comment, ClubCommentsAndRepliesResponse commentResponse) {
        // Member Writing Comment
        Member commentWritingMember = memberService.getMember(comment.getMember().getId());

        // Member's UniversityInfo
        UniversityCertify memberUniversityCertifyInfo = universityCertifyRepository.findByMember(commentWritingMember).orElseThrow(
                () -> new FlintCustomException(HttpStatus.NOT_FOUND, USER_UNIVERSITY_CERTIFICATION_NOT_FOUND)
        );

        // University Logo Info
        LogoInfoResponse universityLogoInfoByName = assetService.getUniversityLogoInfoByName(memberUniversityCertifyInfo.getUniversity());

        commentResponse.setUnivInfo(universityLogoInfoByName);
        commentResponse.setMemberName(commentWritingMember.getName());
        commentResponse.setMemberMajor(memberUniversityCertifyInfo.getMajor());
        commentResponse.setCommentContent(comment.getContents());

        if (comment.getCommentChildren().isEmpty()) {
            return commentResponse;
        } else {
            for (ClubComment commentChild : comment.getCommentChildren()) {
                ClubCommentsAndRepliesResponse commentReplyResponse = new ClubCommentsAndRepliesResponse();
                commentResponse.addCommentReply(addCommentsAndReplies(commentChild, commentReplyResponse));
            }
        }
        return commentResponse;
    }

}
