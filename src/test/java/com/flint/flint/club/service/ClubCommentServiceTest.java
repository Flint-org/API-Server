package com.flint.flint.club.service;

import com.flint.flint.asset.domain.UniversityAsset;
import com.flint.flint.asset.repository.UniversityAssetRepository;
import com.flint.flint.club.domain.comment.ClubComment;
import com.flint.flint.club.domain.main.Club;
import com.flint.flint.club.repository.comment.ClubCommentRepository;
import com.flint.flint.club.repository.main.ClubRepository;
import com.flint.flint.club.request.ClubCommentCreateRequest;
import com.flint.flint.club.response.ClubCommentsAndRepliesResponse;
import com.flint.flint.club.service.comment.ClubCommentServiceImpl;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.domain.main.UniversityCertify;
import com.flint.flint.member.repository.UniversityCertifyRepository;
import com.flint.flint.member.spec.Authority;
import com.flint.flint.member.repository.MemberRepository;
import com.flint.flint.member.service.MemberService;
import com.flint.flint.security.auth.dto.AuthorityMemberDTO;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class ClubCommentServiceTest {
    @Autowired
    ClubRepository clubRepository;
    @Autowired
    ClubCommentRepository clubCommentRepository;
    @Autowired
    ClubCommentServiceImpl clubCommentService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    UniversityCertifyRepository universityCertifyRepository;
    @Autowired
    UniversityAssetRepository universityAssetRepository;

    @BeforeEach
    void init() {
        memberRepository.deleteAll();
        clubRepository.deleteAll();

        Club mockClub = Club.builder()
                .name("모임 테스트")
                .build();
        clubRepository.save(mockClub);

        Member member = Member.builder()
                .name("김기현")
                .authority(Authority.AUTHUSER)
                .email("test@test")
                .providerName("kakao")
                .providerId("test")
                .build();
        memberRepository.save(member);

        AuthorityMemberDTO authMember = AuthorityMemberDTO.builder()
                .id(memberRepository.findAll().get(0).getId())
                .email("test@test")
                .build();

        SecurityContext context = SecurityContextHolder.getContext();
        Collection<? extends GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_AUTHUSER"));
        context.setAuthentication(new UsernamePasswordAuthenticationToken(authMember, "", authorities));

    }

    @Test
    @DisplayName("모임 댓글 생성 테스트")
    @Transactional
    void test1() throws Exception {
        // given

        ClubCommentCreateRequest request = ClubCommentCreateRequest.builder()
                .contents("모임 댓글 테스트").build();

        AuthorityMemberDTO authMember = AuthorityMemberDTO.builder()
                .id(memberRepository.findAll().get(0).getId())
                .email("test@test")
                .build();

        // when
        clubCommentService.createComment(authMember, request, clubRepository.findAll().get(0).getId());

        // then
        assertEquals(request.getContents(), clubCommentRepository.findAll().get(0).getContents());
    }

    @Test
    @DisplayName("모임 대댓글 생성 테스트")
    @Transactional
    void test2() {
        // given

        ClubComment mockComment = ClubComment.builder()
                .club(clubRepository.findAll().get(0))
                .contents("부모 댓글")
                .member(memberRepository.findAll().get(0))
                .build();
        clubCommentRepository.save(mockComment);

        AuthorityMemberDTO authMember = AuthorityMemberDTO.builder()
                .id(memberRepository.findAll().get(0).getId())
                .email("test@test")
                .build();

        ClubCommentCreateRequest request1 = ClubCommentCreateRequest.builder()
                .contents("모임 대댓글 테스트1").build();
        ClubCommentCreateRequest request2 = ClubCommentCreateRequest.builder()
                .contents("모임 대댓글 테스트2").build();

        // when
        clubCommentService.createCommentReply(
                authMember,
                request1,
                clubRepository.findAll().get(0).getId(),
                clubCommentRepository.findAll().get(0).getId());
        clubCommentService.createCommentReply(
                authMember,
                request2,
                clubRepository.findAll().get(0).getId(),
                clubCommentRepository.findAll().get(0).getId());

        // then
        ClubComment parentComment = clubCommentRepository.findAll().get(0);
        ClubComment replyComment1 = clubCommentRepository.findAll().get(1);
        ClubComment replyComment2 = clubCommentRepository.findAll().get(2);

        assertEquals(parentComment.getContents(), replyComment2.getCommentParent().getContents());
        assertEquals(parentComment.getContents(), replyComment1.getCommentParent().getContents());
        int i = 1;
        for (ClubComment commentChild : parentComment.getCommentChildren()) {
            assertEquals(commentChild.getContents(), "모임 대댓글 테스트"+i);
            i++;
        }
        assertEquals(parentComment.getMember(), memberService.getMember(authMember.getId()));
    }

    @Test
    @DisplayName("모임 댓글과 대댓글 조회 테스트")
    @Transactional(readOnly = true)
    void test3() {
        // given
        // 학교 정보
        UniversityCertify memberUnivInfo = UniversityCertify.builder()
                .memeber(memberRepository.findAll().get(0))
                .major("소프트웨어학과")
                .university("가천대학교")
                .build();
        universityCertifyRepository.save(memberUnivInfo);
        UniversityAsset univAsset = UniversityAsset.builder()
                .red(100)
                .green(101)
                .blue(102)
                .logoUrl("logo.com")
                .universityName("가천대학교")
                .emailSuffix("gachon.ac.kr")
                .build();
        universityAssetRepository.save(univAsset);

        // 부모 댓글 1과 자식 3개
        ClubComment mockComment1 = ClubComment.builder()
                .club(clubRepository.findAll().get(0))
                .contents("부모 댓글 1")
                .member(memberRepository.findAll().get(0))
                .build();
        mockComment1.setCommentParent(null);

        ClubComment mockComment1Reply1 = ClubComment.builder()
                .club(clubRepository.findAll().get(0))
                .member(memberRepository.findAll().get(0))
                .contents("부모 댓글 1의 대댓글 1")
                .build();
        mockComment1Reply1.setCommentParent(mockComment1);
        ClubComment mockComment1Reply2 = ClubComment.builder()
                .club(clubRepository.findAll().get(0))
                .member(memberRepository.findAll().get(0))
                .contents("부모 댓글 1의 대댓글 2")
                .build();
        mockComment1Reply2.setCommentParent(mockComment1);
        ClubComment mockComment1Reply3 = ClubComment.builder()
                .club(clubRepository.findAll().get(0))
                .member(memberRepository.findAll().get(0))
                .contents("부모 댓글 1의 대댓글 3")
                .build();
        mockComment1Reply3.setCommentParent(mockComment1);
        mockComment1.addCommentChildren(mockComment1Reply1);
        mockComment1.addCommentChildren(mockComment1Reply2);
        mockComment1.addCommentChildren(mockComment1Reply3);
        clubCommentRepository.save(mockComment1);

        // 부모 댓글 2와 자식 0개
        ClubComment mockComment2 = ClubComment.builder()
                .club(clubRepository.findAll().get(0))
                .contents("부모 댓글 2")
                .member(memberRepository.findAll().get(0))
                .build();
        mockComment2.setCommentParent(null);
        clubCommentRepository.save(mockComment2);

        // 부모 댓글 3과 자식 3개
        ClubComment mockComment3 = ClubComment.builder()
                .club(clubRepository.findAll().get(0))
                .contents("부모 댓글 3")
                .member(memberRepository.findAll().get(0))
                .build();
        mockComment3.setCommentParent(null);

        ClubComment mockComment3Reply1 = ClubComment.builder()
                .club(clubRepository.findAll().get(0))
                .member(memberRepository.findAll().get(0))
                .contents("부모 댓글 3의 대댓글 1")
                .build();
        mockComment3Reply1.setCommentParent(mockComment3);
        ClubComment mockComment3Reply2 = ClubComment.builder()
                .club(clubRepository.findAll().get(0))
                .member(memberRepository.findAll().get(0))
                .contents("부모 댓글 3의 대댓글 2")
                .build();
        mockComment3Reply2.setCommentParent(mockComment3);
        ClubComment mockComment3Reply3 = ClubComment.builder()
                .club(clubRepository.findAll().get(0))
                .member(memberRepository.findAll().get(0))
                .contents("부모 댓글 3의 대댓글 3")
                .build();
        mockComment3Reply3.setCommentParent(mockComment3);

        mockComment3.addCommentChildren(mockComment3Reply1);
        mockComment3.addCommentChildren(mockComment3Reply2);
        mockComment3.addCommentChildren(mockComment3Reply3);
        clubCommentRepository.save(mockComment3);

        // when
        List<ClubCommentsAndRepliesResponse> comments = clubCommentService.getComments(clubRepository.findAll().get(0).getId());

        // then
        assertEquals(3, comments.size());
        for(ClubCommentsAndRepliesResponse response: comments) {
            assertEquals(response.getUnivInfo().getUniversityName(), "가천대학교");
        }
        int i = 1;
        for(ClubCommentsAndRepliesResponse response: comments) {
            assertEquals(response.getCommentContent(), "부모 댓글 " + i);
            i++;
        }
        int j = 1;
        for(ClubCommentsAndRepliesResponse response: comments) {
            int k = 1;
            for (ClubCommentsAndRepliesResponse commentReply : response.getCommentReplies()) {
                assertEquals(commentReply.getCommentContent(), "부모 댓글 " + j +"의 대댓글 " + k);
                k++;
            }
            j++;
        }
    }
}
