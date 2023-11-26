package com.flint.flint.community.service;

import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.common.spec.ResultCode;
import com.flint.flint.community.domain.board.Board;
import com.flint.flint.community.domain.post.Post;
import com.flint.flint.community.domain.post.PostLike;
import com.flint.flint.community.dto.request.PostRequest;
import com.flint.flint.community.dto.response.PostListResponse;
import com.flint.flint.community.dto.response.PostPreSignedUrlResponse;
import com.flint.flint.community.repository.BoardRepository;
import com.flint.flint.community.repository.post.PostRepository;
import com.flint.flint.community.spec.BoardType;
import com.flint.flint.community.spec.SortStrategy;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.repository.MemberRepository;
import com.flint.flint.member.spec.Authority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void init() {
        Board board1 = Board.builder()
                .boardType(BoardType.GENERAL)
                .generalBoardName("연애게시판")
                .build();

        Board board2 = Board.builder()
                .boardType(BoardType.GENERAL)
                .generalBoardName("비밀게시판")
                .build();

        boardRepository.save(board1);
        boardRepository.save(board2);

        Member member = Member.builder()
                .name("홍길동1")
                .authority(Authority.AUTHUSER)
                .email("test1@test.com")
                .providerId("kakao test1")
                .providerName("kakao")
                .build();

        Member member2 = Member.builder()
                .name("홍길동2")
                .authority(Authority.AUTHUSER)
                .email("test2@test.com")
                .providerId("kakao test2")
                .providerName("kakao")
                .build();

        memberRepository.save(member);
        memberRepository.save(member2);

        Post post1 = Post.builder()
                .title("제목1")
                .contents("내용1")
                .board(board1)
                .build();

        Post post2 = Post.builder()
                .title("제목2")
                .contents("내용2")
                .board(board1)
                .build();

        Post post3 = Post.builder()
                .title("제목3")
                .contents("내용3")
                .board(board1)
                .build();

        Post post4 = Post.builder()
                .title("제목4")
                .contents("내용4")
                .board(board1)
                .build();

        Post post5 = Post.builder()
                .title("제목5")
                .contents("내용5")
                .board(board1)
                .build();

        Post post6 = Post.builder()
                .title("제목6")
                .contents("내용6")
                .board(board2)
                .build();

        Post post7 = Post.builder()
                .title("제목7")
                .contents("내용7")
                .board(board2)
                .build();

        Post post8 = Post.builder()
                .title("제목8")
                .contents("내용8")
                .board(board2)
                .build();

        PostLike postLike1 = PostLike.builder()
                .member(member)
                .build();

        PostLike postLike2 = PostLike.builder()
                .member(member2)
                .build();

        PostLike postLike3 = PostLike.builder()
                .member(member)
                .build();

        post5.addPostLike(postLike1);
        post5.addPostLike(postLike2);
        post3.addPostLike(postLike3);

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);
        postRepository.save(post5);
        postRepository.save(post6);
        postRepository.save(post7);
        postRepository.save(post8);
    }

    @Test
    @DisplayName("Pre-Signed URL 방식의 게시글 생성에 성공한다.")
    void createPost() {
        // given
        Member member = Member.builder()
                .name("홍길동")
                .authority(Authority.AUTHUSER)
                .email("test@test.com")
                .providerId("kakao test")
                .providerName("kakao")
                .build();

        Member savedMember = memberRepository.save(member);

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

        // when
        List<PostPreSignedUrlResponse> responses = postService.createPost(savedMember.getProviderId(), postRequest);

        List<Post> postList = postRepository.findAll();

        // then
        assertEquals(3, responses.size());

        assertEquals("/static/abcd.jpg", postList.get(postList.size() - 1).getPostImages().get(0).getImgUrl());
        assertEquals("제목입니다", postList.get(postList.size() - 1).getTitle());
        assertEquals(BoardType.GENERAL, postList.get(postList.size() - 1).getBoard().getBoardType());
        assertEquals("자유게시판", postList.get(postList.size() - 1).getBoard().getGeneralBoardName());
        assertEquals("홍길동", postList.get(postList.size() - 1).getMember().getName());
    }

    @Test
    @DisplayName("게시글을 저장할 때 파일 형식이 이미지가 아니라면 예외가 발생한다.")
    void createPostWithImageType() {
        // given
        Member member = Member.builder()
                .name("홍길동")
                .authority(Authority.AUTHUSER)
                .email("test@test.com")
                .providerId("kakao test")
                .providerName("kakao")
                .build();

        Member savedMember = memberRepository.save(member);

        Board board = Board.builder()
                .boardType(BoardType.GENERAL)
                .generalBoardName("자유게시판")
                .build();

        Board savedBoard = boardRepository.save(board);

        List<String> fileNames = new ArrayList<>();
        fileNames.add("abcd.jpg");
        fileNames.add("efgh.xxxx");

        PostRequest postRequest = PostRequest.builder()
                .title("제목입니다")
                .contents("게시글을 생성하는 테스트 코드입니다")
                .fileNames(fileNames)
                .boardId(savedBoard.getId())
                .build();

        // then, when
        assertThatThrownBy(() -> postService.createPost(savedMember.getProviderId(), postRequest))
                .isInstanceOf(FlintCustomException.class)
                .hasMessage(ResultCode.INVALID_IMAGE_EXTENSION_TYPE.getMessage());
    }

    @DisplayName("전체 게시판에 대해 게시글 목록을 최신순과 페이징으로 조회에 성공한다.")
    @Test
    void getPostByPagingTestWithAllBoard() {
        // given
        Long cursor1 = null;
        long size = 2;

        // when
        List<PostListResponse> posts = postService.getPostsByPaging(null, cursor1, size, null);

        Long cursor2 = posts.get(posts.size() - 1).getPostId();
        List<PostListResponse> posts2 = postService.getPostsByPaging(null, cursor2, size, null);

        // then
        assertEquals("제목8", posts.get(0).getTitle());
        assertEquals("제목7", posts.get(1).getTitle());

        assertEquals("제목6", posts2.get(0).getTitle());
        assertEquals("제목5", posts2.get(1).getTitle());
    }

    @DisplayName("특정 게시판에 대해 게시글 목록을 최신순과 페이징으로 조회에 성공한다.")
    @Test
    void getPostByPagingTestWithSpecificBoard() {
        // given
        Board board = boardRepository.findBoardByGeneralBoardName("연애게시판").get();
        Long cursor = null;
        long size = 2;

        // when
        List<PostListResponse> posts = postService.getPostsByPaging(board.getId(), cursor, size, null);

        Long cursor2 = posts.get(posts.size() - 1).getPostId();
        List<PostListResponse> posts2 = postService.getPostsByPaging(null, cursor2, size, null);

        // then
        assertEquals("제목5", posts.get(0).getTitle());
        assertEquals("제목4", posts.get(1).getTitle());

        assertEquals("제목3", posts2.get(0).getTitle());
        assertEquals("제목2", posts2.get(1).getTitle());
    }

    @DisplayName("게시글 목록을 인기순(좋아요순)과 페이징으로 조회에 성공한다.")
    @Test
    void getPostByPagingTestWithLikeOrder() {
        // given
        Long cursor = null;
        long size = 3;
        SortStrategy strategy = SortStrategy.POPULAR;

        // when
        List<PostListResponse> posts = postService.getPostsByPaging(null, cursor, size, strategy);

        Long cursor2 = posts.get(posts.size() - 1).getPostId();
        List<PostListResponse> posts2 = postService.getPostsByPaging(null, cursor2, size, strategy);

        // then
        assertEquals("제목5", posts.get(0).getTitle());
        assertEquals("제목3", posts.get(1).getTitle());
        assertEquals("제목8", posts.get(2).getTitle());

        assertEquals("제목7", posts2.get(0).getTitle());
        assertEquals("제목6", posts2.get(1).getTitle());
        assertEquals("제목4", posts2.get(2).getTitle());
    }

    @DisplayName("게시글 조회할 때 커서 값이 null 또는 양수가 아니면 처음부터 조회에 성공한다.")
    @Test
    void getPostByPagingTestWithInitialCursor() {
        // given
        Long cursor = 0L;
        long size = 3;

        // when
        List<PostListResponse> posts = postService.getPostsByPaging(null, cursor, size, null);

        // then
        assertEquals("제목8", posts.get(0).getTitle());
        assertEquals("제목7", posts.get(1).getTitle());
        assertEquals("제목6", posts.get(2).getTitle());
    }

    @DisplayName("게시글을 조회할 때 가져올 페이지가 유효하지 않으면 예외가 발생한다.")
    @Test
    void getPostByPagingTestWithInvalidPageSize() {
        // given
        Long cursor = null;
        Long size = null;
        long size2 = 0;
        long size3 = -1;

        // when, then
        assertThatThrownBy(() -> postService.getPostsByPaging(null, cursor, size, SortStrategy.POPULAR))
                .isInstanceOf(FlintCustomException.class)
                .hasMessage(ResultCode.INVALID_PAGE_SIZE.getMessage());

        assertThatThrownBy(() -> postService.getPostsByPaging(null, cursor, size2, SortStrategy.POPULAR))
                .isInstanceOf(FlintCustomException.class)
                .hasMessage(ResultCode.INVALID_PAGE_SIZE.getMessage());

        assertThatThrownBy(() -> postService.getPostsByPaging(null, cursor, size3, SortStrategy.POPULAR))
                .isInstanceOf(FlintCustomException.class)
                .hasMessage(ResultCode.INVALID_PAGE_SIZE.getMessage());
    }

    @DisplayName("게시글을 조회할 때 현재 커서가 존재하지 않거나 초과하면 예외가 발생한다.")
    @Test
    void getPostByPagingTestWithInvalidCursor() {
        // given
        Long cursor = null;
        Long size = 6L;

        // when
        List<PostListResponse> posts = postService.getPostsByPaging(null, cursor, size, null);
        Long cursor2 = posts.get(posts.size() - 1).getPostId();

        List<PostListResponse> posts2 = postService.getPostsByPaging(null, cursor2, size, null);
        Long cursor3 = posts2.get(posts2.size() - 1).getPostId(); // 가장 마지막 데이터

        List<PostListResponse> posts3 = postService.getPostsByPaging(null, cursor3, size, null);

        // then
        assertEquals(0, posts3.size());
        assertThatThrownBy(() -> postService.getPostsByPaging(null, posts.get(0).getPostId() + 1, size, null))
                .isInstanceOf(FlintCustomException.class)
                .hasMessage(ResultCode.INVALID_POST_PAGE_CURSOR.getMessage());
    }
}