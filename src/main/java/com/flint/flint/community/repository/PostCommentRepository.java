package com.flint.flint.community.repository;

import com.flint.flint.community.domain.post.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
}
