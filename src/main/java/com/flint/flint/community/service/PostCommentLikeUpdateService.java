package com.flint.flint.community.service;

import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.common.spec.ResultCode;
import com.flint.flint.community.domain.post.PostComment;
import com.flint.flint.community.domain.post.PostCommentLike;
import com.flint.flint.community.repository.PostCommentLikeRepository;
import com.flint.flint.community.repository.PostCommentRepository;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public void createPostCommentLike(String providerId, long postCommentId) {
        Optional<PostCommentLike> OptionalCommentLike = postCommentLikeRepository.findByProviderIdAndPostCommentId(providerId, postCommentId);
        if(!OptionalCommentLike.isPresent()) {  //이전에 좋아요를 안했을 때
            Member member = memberService.getMemberByProviderId(providerId);
            PostComment postComment = postCommentRepository.findById(postCommentId).orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, ResultCode.POST_COMMENT_NOT_FOUND));
            PostCommentLike build = PostCommentLike.builder()
                    .member(member)
                    .postComment(postComment)
                    .build();
            postCommentLikeRepository.save(build);
        }
        else postCommentLikeRepository.delete(OptionalCommentLike.get()); // 이전에 좋아요를 했을 때
    }
}
