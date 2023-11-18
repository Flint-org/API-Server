package com.flint.flint.community.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.flint.flint.community.domain.board.Board;
import com.flint.flint.community.domain.post.Post;
import com.flint.flint.community.domain.post.PostScrap;
import com.flint.flint.community.dto.request.PostRequest;
import com.flint.flint.community.repository.BoardRepository;
import com.flint.flint.community.repository.PostRepository;
import com.flint.flint.community.repository.PostScrapRepository;
import com.flint.flint.community.spec.BoardType;
import com.flint.flint.custom_member.WithMockCustomMember;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class PostApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostScrapRepository postScrapRepository;
    @Autowired
    private PostLikeRepository postLikeRepository;

    private static final String BASE_URL = "/api/v1/posts";

    @Test
    @DisplayName("학교 인증을 받지 않은 회원은 게시글 생성 시 예외가 발생한다.")
    void createPostWithoutCredential() throws Exception {
        Board board = Board.builder()
                .boardType(BoardType.GENERAL)
                .generalBoardName("자유게시판")
                .build();

        Board savedBoard = boardRepository.save(board);
        List<String> fileNames = new ArrayList<>();
        fileNames.add("abcd.jpg");
        fileNames.add("efgh.jpg");
        fileNames.add("ijkl.jpg");

        PostRequest postRequest = PostRequest.builder()
                .title("제목입니다")
                .contents("게시글을 생성하는 테스트 코드입니다")
                .fileNames(fileNames)
                .boardId(savedBoard.getId())
                .build();

        String json = convertDtoToJson(postRequest);

        this.mockMvc.perform(post(BASE_URL)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("학교 인증을 받은 회원은 게시글 생성에 성공한다.")
    @WithMockCustomMember
    void createPost() throws Exception {
        Board board = Board.builder()
                .boardType(BoardType.GENERAL)
                .generalBoardName("자유게시판")
                .build();

        Board savedBoard = boardRepository.save(board);

        Member member = Member.builder()
                .name("테스터")
                .email("test@test.com")
                .providerName("kakao")
                .providerId("kakao test")
                .build();

        memberRepository.save(member);

        List<String> fileNames = new ArrayList<>();
        fileNames.add("abcd.jpg");
        fileNames.add("efgh.jpg");
        fileNames.add("ijkl.jpg");

        PostRequest postRequest = PostRequest.builder()
                .title("제목입니다")
                .contents("게시글을 생성하는 테스트 코드입니다")
                .fileNames(fileNames)
                .boardId(savedBoard.getId())
                .build();

        String json = convertDtoToJson(postRequest);

        this.mockMvc.perform(post(BASE_URL)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private String convertDtoToJson(Object obj) throws JsonProcessingException {
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(obj);
    }


  @Test
    @DisplayName("게시글 좋아요 생성 테스트")
    @Transactional
    @WithMockCustomMember(role = "ROLE_AUTHUSER")
    void test3() throws Exception {
        //given
        Member member = Member.builder()
                .name("테스터")
                .email("test@test.com")
                .providerName("kakao")
                .providerId("kakao test")
                .build();

        memberRepository.save(member);

        Post post = Post.builder().title("테스트 제목입니다.").contents("테스트 내용입니다.").build();

        postRepository.save(post);

        Long postId = post.getId();

        //when,then
        mockMvc.perform(post(BASE_URL + "/like/" + postId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.postLikeCount").value(1)); // 응답 JSON에서 likeCount 값 확인
    }

    @Test
    @DisplayName("게시글 좋아요 취소 테스트")
    @Transactional
    @WithMockCustomMember(role = "ROLE_AUTHUSER")
    void test4() throws Exception {
        //given
        Member member = Member.builder()
                .name("테스터")
                .email("test@test.com")
                .providerName("kakao")
                .providerId("kakao test")
                .build();

        memberRepository.save(member);

        Post post = Post.builder().title("테스트 제목입니다.").contents("테스트 내용입니다.").build();

        postRepository.save(post);

        PostLike postLike = PostLike.builder()
                .post(post)
                .member(member)
                .build();

        postLikeRepository.save(postLike);


        //when,then
        mockMvc.perform(delete(BASE_URL + "/like/" + post.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 스크랩 생성 테스트")
    @Transactional
    @WithMockCustomMember(role = "ROLE_AUTHUSER")
    void test4() throws Exception {
        //given
        Member member = Member.builder()
                .name("테스터")
                .email("test@test.com")
                .providerName("kakao")
                .providerId("kakao test")
                .build();

        memberRepository.save(member);

        Post post = Post.builder().title("테스트 제목입니다.").contents("테스트 내용입니다.").build();

        postRepository.save(post);


        Long postId = post.getId();

        //when,then
        mockMvc.perform(post(BASE_URL + "/scrap/" + postId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 스크랩 취소 테스트")
    @Transactional
    @WithMockCustomMember(role = "ROLE_AUTHUSER")
    void test5() throws Exception {
        //given
        Member member = Member.builder()
                .name("테스터")
                .email("test@test.com")
                .providerName("kakao")
                .providerId("kakao test")
                .build();

        Post post = Post.builder().title("테스트 제목입니다.").contents("테스트 내용입니다.").build();

        PostScrap postScrap = PostScrap.builder().post(post).member(member).build();
        memberRepository.save(member);
        postRepository.save(post);
        postScrapRepository.save(postScrap);

        //when,then
        mockMvc.perform(delete(BASE_URL+"/scrap/" + post.getId()))
                .andExpect(status().isOk());
    }
}