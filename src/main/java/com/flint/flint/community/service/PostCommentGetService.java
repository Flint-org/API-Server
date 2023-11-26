package com.flint.flint.community.service;

import com.flint.flint.community.dto.response.PostCommentResponse;
import com.flint.flint.community.repository.custom.PostCommentRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 정순원
 * @since 2023-10-09
 */
@Service
@RequiredArgsConstructor
public class PostCommentGetService {

    private final PostCommentRepositoryCustom postCommentRepositoryCustom;

    @Transactional(readOnly = true)
    public List<PostCommentResponse> getPostComment(long postId) {
        List<PostCommentResponse> commentResponses = postCommentRepositoryCustom.findByPostId(postId);

        Map<Long, PostCommentResponse> commentMap = new HashMap<>();
        List<PostCommentResponse> parentCommentResponses = new ArrayList<>();

        for (PostCommentResponse commentResponse : commentResponses) {
            commentMap.put(commentResponse.getPostCommentId(), commentResponse);
            if (commentResponse.getParentCommentId() == null) {
                parentCommentResponses.add(commentResponse);
            } else {
                commentMap.get(commentResponse.getParentCommentId()).getReplies().add(commentResponse);
            }
        }

        return parentCommentResponses;
    }
}
