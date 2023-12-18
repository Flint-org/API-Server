package com.flint.flint.community.repository.post_comment;

import com.flint.flint.community.dto.response.PostCommentResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 정순원
 * @since 2023-11-06
 */

@Repository
public interface PostCommentRepositoryCustom {
    List<PostCommentResponse> findByPostId(Long postId);
}
