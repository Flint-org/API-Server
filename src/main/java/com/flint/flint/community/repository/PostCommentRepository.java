package com.flint.flint.community.repository;

import com.flint.flint.community.domain.post.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 정순원
 * @since 2023-10-16
 */
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
}
