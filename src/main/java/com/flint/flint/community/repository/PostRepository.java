package com.flint.flint.community.repository;

import com.flint.flint.community.domain.post.Post;
import com.flint.flint.community.repository.custom.PostRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
}
