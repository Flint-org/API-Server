package com.flint.flint.community.repository.custom;

import com.flint.flint.community.domain.post.PostComment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepositoryCustom {
    List<PostComment> findByPostId(Long postId);
}
