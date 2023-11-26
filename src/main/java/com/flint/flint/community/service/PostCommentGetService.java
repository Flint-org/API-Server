package com.flint.flint.community.service;

import com.flint.flint.community.domain.post.PostComment;
import com.flint.flint.community.dto.response.PostCommentGetResponse;
import com.flint.flint.community.repository.post_comment.PostCommentRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author 정순원
 * @since 2023-10-09
 */
@Service
@RequiredArgsConstructor
public class PostCommentGetService {

    private final PostCommentRepositoryCustom postCommentRepositoryCustom;

    @Transactional
    public List<PostCommentGetResponse> getPostComment(long postId) {
        List<PostComment> postCommentList = postCommentRepositoryCustom.findByPostId(postId);

        List<PostCommentGetResponse> responseList = new ArrayList<>();
        Map<Long, PostCommentGetResponse> responseHashMap = new HashMap<>();

        postCommentList.forEach(postComment -> {
            PostCommentGetResponse postCommentGetResponse = PostCommentGetResponse.convertCommentToDTO(postComment);
            responseHashMap.put(postCommentGetResponse.getPostCommentId(), postCommentGetResponse);

            // 대댓글인 경우 부모 댓글의 replies에 추가
            Optional.ofNullable(postComment.getParentComment())
                    .map(parent -> responseHashMap.get(parent.getId()))
                    .ifPresent(parentResponse -> parentResponse.getReplies().add(postCommentGetResponse));

            // 댓글인 경우 ResponseList에 추가
            if (postComment.getParentComment() == null) {
                responseList.add(postCommentGetResponse);
            }
        });
        return responseList;
    }
}
