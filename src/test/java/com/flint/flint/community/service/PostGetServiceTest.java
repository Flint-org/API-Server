package com.flint.flint.community.service;

import com.flint.flint.asset.domain.UniversityAsset;
import com.flint.flint.asset.repository.UniversityAssetRepository;
import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.common.spec.ResultCode;
import com.flint.flint.community.domain.board.Board;
import com.flint.flint.community.domain.post.Post;
import com.flint.flint.community.domain.post.PostImage;
import com.flint.flint.community.dto.response.DetailPostResponse;
import com.flint.flint.community.repository.BoardRepository;
import com.flint.flint.community.repository.post.PostRepository;
import com.flint.flint.community.spec.BoardType;
import com.flint.flint.idcard.domain.IdCard;
import com.flint.flint.idcard.repository.IdCardJPARepository;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.repository.MemberRepository;
import com.flint.flint.member.spec.Authority;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostGetServiceTest {
    @Autowired
    private PostGetService postGetService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private IdCardJPARepository idCardJPARepository;
    @Autowired
    private UniversityAssetRepository universityAssetRepository;
    @Autowired
    private BoardRepository boardRepository;

    @BeforeEach
    void init() {
        Member member = Member.builder()
                .name("테스터")
                .authority(Authority.AUTHUSER)
                .providerName("kakao")
                .email("test@test.com")
                .providerId("kakao test")
                .build();
        memberRepository.save(member);

        Board board = Board.builder()
                .generalBoardName("자유게시판")
                .boardType(BoardType.GENERAL)
                .build();
        boardRepository.save(board);

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
    }

    @DisplayName("게시글 ID로 게시글 상세 조회 시, 작성자와 게시글 메타데이터, 데이터 정보 조회에 성공한다.")
    @Test
    void getDetailPost() {
        // given
        Member member = memberRepository.findByProviderId("kakao test").get();
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

        post.addImageUrl(image1);
        post.addImageUrl(image2);

        Post savedPost = postRepository.save(post);

        // when
        DetailPostResponse detailPost = postGetService.getDetailPost(savedPost.getId());

        // then
        assertEquals("제목입니다.", detailPost.getTitle());
        assertEquals("내용입니다.", detailPost.getContents());
        assertEquals(2, detailPost.getImages().size());
        assertEquals("가천대학교", detailPost.getWriter().getUniversityName());
        assertEquals("소프트웨어학과", detailPost.getWriter().getMajor());
        assertEquals("/가천", detailPost.getWriter().getUniversityLogoUrl());
    }

    @DisplayName("유효하지 않은 게시글 ID로 상세 조회 시 예외가 발생한다.")
    @Test
    void getDetailPostWithInvalidPost() {
        // given
        Long testId = -1L;

        // when & then
        Assertions.assertThatThrownBy(() -> postGetService.getDetailPost(testId))
                .isInstanceOf(FlintCustomException.class)
                .hasMessage(ResultCode.POST_NOT_FOUND.getMessage());
    }
}