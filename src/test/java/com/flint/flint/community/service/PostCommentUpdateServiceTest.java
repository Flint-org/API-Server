package com.flint.flint.community.service;

import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.common.spec.ResultCode;
import com.flint.flint.community.domain.post.Post;
import com.flint.flint.community.domain.post.PostComment;
import com.flint.flint.community.dto.request.PostCommentUpdateRequest;
import com.flint.flint.community.dto.response.PostCommentUpdateResponse;
import com.flint.flint.community.repository.PostCommentRepository;
import com.flint.flint.community.repository.PostRepository;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.repository.MemberRepository;
import com.flint.flint.member.service.MemberService;
import com.flint.flint.member.spec.Authority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PostCommentUpdateServiceTest {

    @Autowired
    PostCommentRepository postCommentRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostCommentUpdateService postCommentUpdateService;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    Post post;
    PostComment parentComment;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .name("정순원")
                .authority(Authority.AUTHUSER)
                .email("test@test.com")
                .providerName("kakao")
                .providerId("kakao test")
                .build();

        memberRepository.save(member);

        post = Post.builder()
                .title("테스트제목")
                .contents("테스트내용")
                .build();

        postRepository.save(post);


        parentComment = PostComment.builder()
                .contents("부모댓글 테스트내용")
                .post(post)
                .member(member)
                .build();

        postCommentRepository.save(parentComment);
    }

    @Test
    @DisplayName("커뮤니티 댓글 생성 테스트")
    @Transactional
    void test1() {
        //given
        PostCommentUpdateRequest requestDTO = PostCommentUpdateRequest.builder()
                .contents("댓글 테스트내용입니다.")
                .build();
        //when

        PostCommentUpdateResponse responseDTO = postCommentUpdateService.createPostComment("kakao test", post.getId(), requestDTO);

        //then
        PostComment postComment = postCommentRepository.findById(responseDTO.getPostCommentId()).orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, ResultCode.POST_COMMENT_NOT_FOUND));
        assertEquals(requestDTO.getContents(), postComment.getContents());
    }

    @Test
    @DisplayName("커뮤니티 대댓글 생성 테스트")
    @Transactional
    void test2() {
        //given
        PostCommentUpdateRequest requestDTO = new PostCommentUpdateRequest("대댓글 테스트내용입니다.", parentComment.getId());
        //when
        PostCommentCreateResponse responseDTO = postCommentUpdateService.createPostComment("kakao test", post.getId(), requestDTO);

        //then
        PostComment reply = postCommentRepository.findById(responseDTO.getPostCommentId()).orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, ResultCode.POST_COMMENT_NOT_FOUND));
        assertEquals(requestDTO.getContents(), reply.getContents());
        assertEquals(reply, parentComment.getReplies().get(0));
        assertEquals(parentComment, reply.getParentComment());
    }

    @Test
    @DisplayName("커뮤니티 댓글 삭제 테스트")
    @Transactional
    void test3() {
        //given

        //when
        postCommentUpdateService.deletePostComment("kakao test", parentComment.getId());
        //then
        assertEquals(0, postCommentRepository.findAll().size());
    }
}