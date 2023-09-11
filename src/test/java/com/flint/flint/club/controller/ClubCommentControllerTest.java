package com.flint.flint.club.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flint.flint.club.controller.comment.ClubCommentController;
import com.flint.flint.club.domain.main.Club;
import com.flint.flint.club.repository.main.ClubRepository;
import com.flint.flint.club.request.ClubCommentCreateRequest;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.spec.Authority;
import com.flint.flint.member.repository.MemberRepository;
import com.flint.flint.security.auth.dto.AuthorityMemberDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ClubCommentControllerTest {
    @Autowired
    ClubCommentController clubCommentController;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ClubRepository clubRepository;
    @Autowired
    private MemberRepository memberRepository;

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
    @DisplayName("모임 댓글 생성 컨트롤러 테스트")
    @Transactional
    void test1() throws Exception {
        Club club = Club.builder()
                .name("모임 1")
                .build();
        clubRepository.save(club);

        String requestBody = objectMapper.writeValueAsString(
                ClubCommentCreateRequest.builder()
                        .contents("댓글 테스트입니다.")
                        .build()
        );
        String clubId = clubRepository.findAll().get(0).getId().toString();

        mockMvc.perform(post("/api/v1/clubs/{id}/comment", clubId)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
}
