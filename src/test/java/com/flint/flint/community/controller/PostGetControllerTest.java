package com.flint.flint.community.controller;

import com.flint.flint.asset.domain.UniversityAsset;
import com.flint.flint.asset.repository.UniversityAssetRepository;
import com.flint.flint.community.domain.board.Board;
import com.flint.flint.community.domain.post.Post;
import com.flint.flint.community.domain.post.PostImage;
import com.flint.flint.community.repository.BoardRepository;
import com.flint.flint.community.repository.post.PostRepository;
import com.flint.flint.community.spec.BoardType;
import com.flint.flint.custom_member.WithMockCustomMember;
import com.flint.flint.idcard.domain.IdCard;
import com.flint.flint.idcard.repository.IdCardJPARepository;
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
    BoardRepository boardRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    private IdCardJPARepository idCardJPARepository;
    @Autowired
    private UniversityAssetRepository universityAssetRepository;


    @BeforeEach
    void setUp() {

        Board board1 = Board.builder()
                .boardType(BoardType.GENERAL)
                .generalBoardName("자유게시판")
                .build();

        Board board2 = Board.builder()
                .boardType(BoardType.MAJOR)
                .generalBoardName("소프트웨어학부 게시판")
                .build();

        boardRepository.save(board1);
        boardRepository.save(board2);

        Post post1 = Post.builder()
                .board(board1)
                .title("제목1")
                .contents("내용1")
                .build();

        Post post2 = Post.builder()
                .board(board2)
                .title("제목2")
                .contents("내용2")
                .build();

        Post post3 = Post.builder()
                .board(board2)
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

        mockMvc.perform(get("/api/v1/posts/search/all/board")
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

    @Test
    @Transactional
    @DisplayName("특정 게시판에서 키워드 포함한 게시글 조회")
    @WithMockCustomMember(role = "ROLE_UNAUTHUSER")
    void test2() throws Exception {

        mockMvc.perform(get("/api/v1/posts/search/specific/board")
                        .param("boardName", "소프트웨어학부 게시판")
                        .param("keyword", "내용"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.data[0].title").value("제목3"))
                .andExpect(jsonPath("$.data[0].imageURL").value(""))
                .andExpect(jsonPath("$.data[1].title").value("제목2"))
                .andExpect(jsonPath("$.data[1].imageURL").value("url3"));
    }

    @DisplayName("특정 게시글 ID로 상세 게시글 내용을 조회한다.")
    @Test
    @Transactional
    @WithMockCustomMember
    void getDetailPost() throws Exception {
        Member member = Member.builder()
                .name("테스터")
                .email("test@test.com")
                .providerName("kakao")
                .providerId("kakao test")
                .build();

        memberRepository.save(member);
        PostImage image1 = PostImage.builder()
                .imgUrl("url1")
                .build();
        PostImage image2 = PostImage.builder()
                .imgUrl("url1")
                .build();
        Post post = Post.builder()
                .member(member)
                .title("제목입니다.")
                .contents("내용입니다.")
                .board(boardRepository.findBoardByGeneralBoardName("자유게시판").get())
                .build();

        IdCard idCard = IdCard.builder()
                .member(member)
                .university("가천대학교")
                .major("소프트웨어학과")
                .email("test@test.com")
                .admissionYear(2019)
                .build();
        idCardJPARepository.save(idCard);

        UniversityAsset asset = UniversityAsset.builder()
                .universityName("가천대학교")
                .emailSuffix("gachon.ac.kr")
                .red(8)
                .green(56)
                .blue(136)
                .logoUrl("/가천")
                .build();
        universityAssetRepository.save(asset);
        post.addImageUrl(image1);
        post.addImageUrl(image2);

        Post savedPost = postRepository.save(post);
        mockMvc.perform(get("/api/v1/posts/{postId}", savedPost.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.data.title").value("제목입니다."))
                .andExpect(jsonPath("$.data.contents").value("내용입니다."))
                .andExpect(jsonPath("$.data.writer.universityName").value("가천대학교"))
                .andExpect(jsonPath("$.data.images.size()").value(2));
    }
}