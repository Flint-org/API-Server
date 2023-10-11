package com.flint.flint.community.service;

import com.flint.flint.community.domain.post.PostComment;
import com.flint.flint.community.domain.post.PostCommentLike;
import com.flint.flint.community.repository.PostCommentLikeRepository;
import com.flint.flint.community.repository.PostCommentRepository;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostCommentLikeUpdateServiceTest {

    @InjectMocks
    private PostCommentLikeUpdateService postCommentLikeUpdateService;

    @Mock
    private PostCommentLikeRepository postCommentLikeRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private PostCommentRepository postCommentRepository;


    @Test
    @DisplayName("기존에 좋아요가 없을 때 좋아요 생성 테스트")
    public void test1() {
        // given
        String providerId = "someProviderId";
        long postCommentId = 1L;

        when(postCommentLikeRepository.findByProviderIdAndPostCommentId(providerId, postCommentId)).thenReturn(Optional.empty());
        when(memberService.getMemberByProviderId(providerId)).thenReturn(new Member());
        when(postCommentRepository.findById(postCommentId)).thenReturn(Optional.of(new PostComment()));

        // when
        postCommentLikeUpdateService.createPostCommentLike(providerId, postCommentId);

        // then
        verify(postCommentLikeRepository, times(1)).save(any(PostCommentLike.class));
        verify(postCommentLikeRepository, never()).delete(any(PostCommentLike.class));
    }

    @Test
    @DisplayName("기존에 좋아요가 있을 때 좋아요 삭제 테스트")
    public void test2() {
        // given
        String providerId = "someProviderId";
        long postCommentId = 1L;

        when(postCommentLikeRepository.findByProviderIdAndPostCommentId(providerId, postCommentId)).thenReturn(Optional.of(new PostCommentLike()));

        // when
        postCommentLikeUpdateService.createPostCommentLike(providerId, postCommentId);

        // then
        verify(postCommentLikeRepository, never()).save(any(PostCommentLike.class));
        verify(postCommentLikeRepository, times(1)).delete(any(PostCommentLike.class));
    }
}
