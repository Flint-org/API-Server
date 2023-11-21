package com.flint.flint.community.service;

import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.common.spec.ResultCode;
import com.flint.flint.community.domain.post.PostComment;
import com.flint.flint.community.domain.post.PostCommentLike;
import com.flint.flint.community.dto.response.PostCommentLikeResponse;
import com.flint.flint.community.repository.PostCommentLikeRepository;
import com.flint.flint.community.repository.post_comment.PostCommentRepository;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 정순원
 * @since 2023-10-11
 */
@Service
@RequiredArgsConstructor
public class PostCommentLikeUpdateService {

    private final PostCommentLikeRepository postCommentLikeRepository;
    private final MemberService memberService;
    private final PostCommentRepository postCommentRepository;

    @Transactional
    public PostCommentLikeResponse createPostCommentLike(String providerId, long postCommentId) {
        Member member = memberService.getMemberByProviderId(providerId);
        PostComment postComment = postCommentRepository.findById(postCommentId).orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, ResultCode.POST_COMMENT_NOT_FOUND));
        PostCommentLike build = PostCommentLike.builder()
                .member(member)
                .postComment(postComment)
                .build();
        postCommentLikeRepository.save(build);

        int likeCount = postCommentLikeRepository.countByPostCommentId(postCommentId);
        return new PostCommentLikeResponse(likeCount);
    }

    @Transactional
    public void deletePostCommentLike(String providerId, long postCommentId) {
        PostCommentLike postCommentLike = postCommentLikeRepository.findByProviderIdAndPostCommentId(providerId, postCommentId).orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, ResultCode.POST_COMMENT_LIKE_NOT_FOUND));
        postCommentLikeRepository.delete(postCommentLike);
    }
}
