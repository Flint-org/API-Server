package com.flint.flint.community.controller;

import com.flint.flint.community.domain.post.PostComment;
import com.flint.flint.community.domain.post.PostCommentLike;
import com.flint.flint.community.repository.PostCommentLikeRepository;
import com.flint.flint.community.repository.PostCommentRepository;
import com.flint.flint.custom_member.WithMockCustomMember;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostCommentUpdateControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    PostCommentUpdateController postCommentUpdateController;
    @Autowired
    PostCommentRepository postCommentRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostCommentLikeRepository postCommentLikeRepository;

    PostComment postComment;
    Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .name("테스터")
                .email("test@test.com")
                .providerName("kakao")
                .providerId("kakao test")
                .build();

        memberRepository.save(member);

        postComment = PostComment.builder().contents("테스트 내용입니다.").build();
        postCommentRepository.save(postComment);
    }

    @Test
    @DisplayName("게시글 댓글 좋아요 생성 테스트")
    @Transactional
    @WithMockCustomMember(role = "ROLE_AUTHUSER")
    void test1() throws Exception {
        //when,then
        mockMvc.perform(post("/api/v1/posts/comment/like/" + postComment.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.postCommentLikeCount").value(1));
    }

    @Test
    @DisplayName("게시글 댓글 좋아요 취소 테스트")
    @Transactional
    @WithMockCustomMember(role = "ROLE_AUTHUSER")
    void test2() throws Exception {
        //given
        PostCommentLike postCommentLike = PostCommentLike.builder()
                .postComment(postComment)
                .member(member)
                .build();
        postCommentLikeRepository.save(postCommentLike);

        //when,then
        mockMvc.perform(delete("/api/v1/posts/comment/like/" + postComment.getId()))
                .andExpect(status().isOk());
    }
}