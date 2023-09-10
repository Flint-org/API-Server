package com.flint.flint.club.service.comment;

import com.flint.flint.club.domain.comment.ClubComment;
import com.flint.flint.club.domain.main.Club;
import com.flint.flint.club.repository.comment.ClubCommentRepository;
import com.flint.flint.club.repository.main.ClubRepository;
import com.flint.flint.club.request.ClubCommentCreateRequest;
import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.service.MemberService;
import com.flint.flint.security.auth.dto.AuthorityMemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.regex.Pattern;

import static com.flint.flint.common.spec.ResultCode.*;

@RequiredArgsConstructor
@Service
public class ClubCommentServiceImpl {
    //todo : [유효성 검사] 댓글은 한글, 영문, 숫자, 특수문자, 이모티콘 공백 포함 최대 300자를 입력 할 수 있습니다.
    //todo : [댓글 조회] 사용자는 댓글 작성자의 대학교로고, 대학교 이름, 학과를 확인 할 수 있습니다.
    //todo : 모임장의 댓글의 경우 모임장 표시
    //todo : [댓글 조회] 댓글은 시간순으로 정렬되며, 6개 이상일때, 앞에 5개의 댓글이 먼저 노출되고 댓글 더 보기 클릭 시 나머지 댓글들이 노출됩니다.
    private final ClubCommentRepository clubCommentRepository;
    private final ClubRepository clubRepository;
    private final MemberService memberService;

    private static final String COMMENT_PATTERN = "^(?=.*[ㄱ-ㅎ가-힣a-zA-Z0-9!@#$%^&*()-_+=<>?]).{1,255}$";
    private static final Pattern COMMENT_REGEX = Pattern.compile(COMMENT_PATTERN);

    @Transactional
    public void createComment(@AuthenticationPrincipal AuthorityMemberDTO memberDTO,
                              ClubCommentCreateRequest request,
                              long clubId) {
        checkIsValidComment(request.getContents());
        clubCommentRepository.save(buildCommonComment(memberDTO, request, clubId));
    }

    @Transactional
    public void createCommentReply(@AuthenticationPrincipal AuthorityMemberDTO memberDTO,
                                   @RequestBody ClubCommentCreateRequest request,
                                   @PathVariable("id") long clubId,
                                   @RequestParam long parentCommentId) {
        ClubComment parentComment = clubCommentRepository.findById(parentCommentId).orElseThrow(
                () -> new FlintCustomException(HttpStatus.NOT_FOUND, CLUB_COMMENT_NOT_FOUND_ERROR)
        );

        ClubComment createdCommentReply = buildCommonComment(memberDTO, request, clubId);
        createdCommentReply.setCommentParent(parentComment);
        parentComment.setCommentChildren(createdCommentReply);
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
        if(!COMMENT_REGEX.matcher(contexts).matches()) {
            throw new FlintCustomException(HttpStatus.BAD_REQUEST, CLUB_COMMENT_PATTERN_NOT_MATCH);
        }
    }
}
