package com.flint.flint.community.service;

import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.common.spec.ResultCode;
import com.flint.flint.community.domain.board.Board;
import com.flint.flint.community.domain.post.Post;
import com.flint.flint.community.dto.request.PostRequest;
import com.flint.flint.community.dto.response.PostPreSignedUrlResponse;
import com.flint.flint.community.repository.BoardRepository;
import com.flint.flint.community.repository.PostRepository;
import com.flint.flint.community.spec.BoardType;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.repository.MemberRepository;
import com.flint.flint.member.spec.Authority;
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

        assertEquals("/static/abcd.jpg", postList.get(0).getPostImages().get(0).getImgUrl());
        assertEquals("제목입니다", postList.get(0).getTitle());
        assertEquals(BoardType.GENERAL, postList.get(0).getBoard().getBoardType());
        assertEquals("자유게시판", postList.get(0).getBoard().getGeneralBoardName());
        assertEquals("홍길동", postList.get(0).getMember().getName());
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
}