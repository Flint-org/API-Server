package com.flint.flint.community.controller;

import com.flint.flint.community.domain.post.Post;
import com.flint.flint.community.domain.post.PostComment;
import com.flint.flint.community.repository.PostCommentRepository;
import com.flint.flint.community.repository.PostRepository;
import com.flint.flint.custom_member.WithMockCustomMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostCommentGetControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PostCommentGetController postCommentGetController;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostCommentRepository postCommentRepository;
    Post post;

    @BeforeEach
    void setUp() {
        post = Post.builder()
                .contents("테스트 내용입니다.")
                .title("테스트 제목입니다.")
                .build();

        postRepository.save(post);

        PostComment parentComment = PostComment.builder()
                .post(post)
                .contents("테스트 댓글내용입니다.")
                .parentComment(null)
                .build();

        postCommentRepository.save(parentComment);

        PostComment postComment = PostComment.builder()
                .post(post)
                .contents("테스트 대댓글내용입니다.")
                .parentComment(parentComment)
                .build();

        postCommentRepository.save(postComment);
    }

    @Test
    @Transactional
    @DisplayName("특정포스트 댓글 조회 테스트 ")
    @WithMockCustomMember(role = "ROLE_UNAUTHUSER")
    void test1() throws Exception {

        Long postId = post.getId();

        mockMvc.perform(get("/api/v1/posts/comment/" + postId))
                .andExpect(status().isOk());
    }
}