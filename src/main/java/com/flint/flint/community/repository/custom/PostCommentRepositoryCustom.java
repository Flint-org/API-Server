package com.flint.flint.community.repository.custom;

import com.flint.flint.community.domain.post.PostComment;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 정순원
 * @since 2023-11-06
 */
@Repository
public interface PostCommentRepositoryCustom {
    List<PostComment> findByPostId(Long postId);
}
