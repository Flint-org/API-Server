package com.flint.flint.community.repository;

import com.flint.flint.community.domain.post.Post;
import com.flint.flint.community.domain.post.PostComment;
import com.flint.flint.member.domain.main.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    PostComment findPostCommentById(Long id);
}
