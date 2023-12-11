package com.flint.flint.community.controller;

import com.flint.flint.asset.domain.UniversityAsset;
import com.flint.flint.asset.repository.UniversityAssetRepository;
import com.flint.flint.community.domain.post.Post;
import com.flint.flint.community.domain.post.PostComment;
import com.flint.flint.community.domain.post.PostCommentLike;
import com.flint.flint.community.repository.PostCommentLikeRepository;
import com.flint.flint.community.repository.post.PostRepository;
import com.flint.flint.community.repository.post_comment.PostCommentRepository;
import com.flint.flint.custom_member.WithMockCustomMember;
import com.flint.flint.idcard.domain.IdCard;
import com.flint.flint.idcard.repository.IdCardJPARepository;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.repository.MemberRepository;
import com.flint.flint.member.spec.Authority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    IdCardJPARepository idCardJPARepository;
    @Autowired
    UniversityAssetRepository assetRepository;
    @Autowired
    PostCommentLikeRepository postCommentLikeRepository;
    Post post;

    @BeforeEach
    void setUp() {
        Member member1 = Member.builder()
                .name("정순원")
                .authority(Authority.AUTHUSER)
                .email("test@test.com")
                .providerName("kakao")
                .providerId("kakao test")
                .build();

        Member member2 = Member.builder()
                .name("김기현")
                .authority(Authority.AUTHUSER)
                .email("test2@test")
                .providerName("naver")
                .providerId("test2")
                .build();

        Member member3 = Member.builder()
                .name("신승건")
                .authority(Authority.AUTHUSER)
                .email("test3@test")
                .providerName("naver")
                .providerId("test3")
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);


        IdCard idcard1 = IdCard.builder()
                .member(member1)
                .major("소프트웨어학부")
                .university("가천대학교")
                .admissionYear(2020)
                .build();

        IdCard idcard2 = IdCard.builder()
                .member(member2)
                .major("소프트웨어학부")
                .university("가천대학교")
                .admissionYear(2018)
                .build();

        IdCard idcard3 = IdCard.builder()
                .member(member3)
                .major("소프트웨어학부")
                .university("가천대학교")
                .admissionYear(2019)
                .build();

        idCardJPARepository.save(idcard1);
        idCardJPARepository.save(idcard2);
        idCardJPARepository.save(idcard3);


        UniversityAsset asset = UniversityAsset.builder()
                .universityName("가천대학교")
                .emailSuffix("gachon.ac.kr")
                .red(8)
                .green(56)
                .blue(136)
                .logoUrl("/가천")
                .build();

        assetRepository.save(asset);

        post = Post.builder()
                .contents("테스트 내용입니다.")
                .title("테스트 제목입니다.")
                .build();

        postRepository.save(post);

        PostComment parentComment1 = PostComment.builder()
                .member(member1)
                .post(post)
                .contents("테스트 1 댓글내용입니다.")
                .build();

        PostComment childComment1 = PostComment.builder()
                .member(member2)
                .post(post)
                .contents("테스트 1-1 대댓글내용입니다.")
                .build();

        PostComment childComment2 = PostComment.builder()
                .member(member3)
                .post(post)
                .contents("테스트 1-2 대댓글내용입니다.")
                .build();

        PostComment parentComment2 = PostComment.builder()
                .member(member2)
                .post(post)
                .contents("테스트 2 댓글내용입니다.")
                .build();

        PostComment childComment3 = PostComment.builder()
                .member(member1)
                .post(post)
                .contents("테스트 2-1 댓글내용입니다.")
                .build();

        parentComment1.addCommentReply(childComment1);
        parentComment1.addCommentReply(childComment2);
        parentComment2.addCommentReply(childComment3);

        postCommentRepository.save(parentComment1);
        postCommentRepository.save(childComment1);
        postCommentRepository.save(childComment2);
        postCommentRepository.save(parentComment2);
        postCommentRepository.save(childComment3);

        PostCommentLike postCommentLike1 = PostCommentLike.builder()
                .postComment(parentComment1)
                .member(member2)
                .build();

        PostCommentLike postCommentLike2 = PostCommentLike.builder()
                .postComment(parentComment1)
                .member(member3)
                .build();


        PostCommentLike postCommentLike3 = PostCommentLike.builder()
                .postComment(parentComment1)
                .member(member1)
                .build();

        PostCommentLike postCommentLike4 = PostCommentLike.builder()
                .postComment(childComment1)
                .member(member2)
                .build();

        PostCommentLike postCommentLike5 = PostCommentLike.builder()
                .postComment(childComment1)
                .member(member3)
                .build();


        PostCommentLike postCommentLike6 = PostCommentLike.builder()
                .postComment(childComment1)
                .member(member1)
                .build();

        postCommentLikeRepository.save(postCommentLike1);
        postCommentLikeRepository.save(postCommentLike2);
        postCommentLikeRepository.save(postCommentLike3);
        postCommentLikeRepository.save(postCommentLike4);
        postCommentLikeRepository.save(postCommentLike5);
        postCommentLikeRepository.save(postCommentLike6);

    }

    @Test
    @Transactional
    @DisplayName("특정포스트 댓글 조회 테스트 ")
    @WithMockCustomMember(role = "ROLE_UNAUTHUSER")
    void test1() throws Exception {

        Long postId = post.getId();

        mockMvc.perform(get("/api/v1/posts/comment/" + postId))
                .andExpect(status().isOk())
                .andDo(print());

    }
}