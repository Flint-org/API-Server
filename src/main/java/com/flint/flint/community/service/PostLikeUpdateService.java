package com.flint.flint.community.service;

import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.common.spec.ResultCode;
import com.flint.flint.community.domain.post.Post;
import com.flint.flint.community.domain.post.PostLike;
import com.flint.flint.community.dto.response.PostLikeResponse;
import com.flint.flint.community.repository.PostLikeRepository;
import com.flint.flint.community.repository.PostRepository;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author 정순원
 * @since 2023-10-16
 */
@Service
@RequiredArgsConstructor
public class PostLikeUpdateService {

    private final PostLikeRepository postLikeRepository;
    private final MemberService memberService;
    private final PostRepository postRepository;


    public PostLikeResponse createPostLike(String providerId, long postId) {
        Optional<PostLike> OptionalPostLike = postLikeRepository.findByProviderIdAndPostCommentId(providerId, postId);
        if (!OptionalPostLike.isPresent()) {  //이전에 좋아요를 안했을 때
            Member member = memberService.getMemberByProviderId(providerId);
            Post post = postRepository.findById(postId).orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, ResultCode.POST_NOT_FOUND));
            PostLike postLike = PostLike.builder()
                    .member(member)
                    .post(post)
                    .build();
            postLikeRepository.save(postLike);
        } else postLikeRepository.delete(OptionalPostLike.get()); // 이전에 좋아요를 했을 때

        int likeCount = postLikeRepository.countByPostId(postId);
        return new PostLikeResponse(likeCount);
    }
}
