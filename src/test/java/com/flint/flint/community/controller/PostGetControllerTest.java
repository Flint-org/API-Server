package com.flint.flint.community.controller;

import com.flint.flint.community.domain.post.Post;
import com.flint.flint.community.domain.post.PostImage;
import com.flint.flint.community.repository.PostRepository;
import com.flint.flint.custom_member.WithMockCustomMember;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostGetControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PostRepository postRepository;

    @Autowired
    EntityManagerFactory emf;

    @BeforeEach
    void setUp() {
        Post post1 = Post.builder()
                .title("제목1")
                .contents("내용1")
                .build();

        Post post2 = Post.builder()
                .title("제목2")
                .contents("내용2")
                .build();

        Post post3 = Post.builder()
                .title("제목3")
                .contents("내용3")
                .build();

        PostImage postImage1 = PostImage.builder()
                .post(post1)
                .imgUrl("url1")
                .build();

        PostImage postImage2 = PostImage.builder()
                .post(post1)
                .imgUrl("url2")
                .build();

        PostImage postImage3 = PostImage.builder()
                .post(post2)
                .imgUrl("url3")
                .build();

        post1.addImageUrl(postImage1);
        post1.addImageUrl(postImage2);
        post2.addImageUrl(postImage3);

        //post1이 url1,url2를 가지고 post2가 url3을 가지고 post1을 이미지 없음
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

    }

    @Test
    @Transactional
    @DisplayName("키워드 포함한 게시글 조회")
    @WithMockCustomMember(role = "ROLE_UNAUTHUSER")
    void test1() throws Exception {

        mockMvc.perform(get("/api/v1/posts/search")
                        .param("keyword", "내용"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.data[0].title").value("제목3"))
                .andExpect(jsonPath("$.data[0].imageURL").value(""))
                .andExpect(jsonPath("$.data[1].title").value("제목2"))
                .andExpect(jsonPath("$.data[1].imageURL").value("url3"))
                .andExpect(jsonPath("$.data[2].title").value("제목1"))
                .andExpect(jsonPath("$.data[2].imageURL").value("url1"));
    }
}