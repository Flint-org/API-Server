package com.flint.flint.club.service;

import com.flint.flint.club.domain.comment.ClubComment;
import com.flint.flint.club.domain.main.Club;
import com.flint.flint.club.repository.comment.ClubCommentRepository;
import com.flint.flint.club.repository.main.ClubRepository;
import com.flint.flint.club.request.ClubCommentCreateRequest;
import com.flint.flint.club.service.comment.ClubCommentServiceImpl;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.domain.spec.Authority;
import com.flint.flint.member.repository.MemberRepository;
import com.flint.flint.member.service.MemberService;
import com.flint.flint.security.auth.dto.AuthorityMemberDTO;
import jakarta.transaction.Transactional;
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

    @BeforeEach
    void init() {
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
        Club mockClub = Club.builder()
                .name("모임 테스트")
                .build();
        clubRepository.save(mockClub);

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
        Club mockClub = Club.builder()
                .name("모임 테스트")
                .build();
        clubRepository.save(mockClub);

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

}
