package com.flint.flint.community.repository;

import com.flint.flint.community.domain.post.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author 정순원
 * @since 2023-10-16
 */
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    @Query("SELECT pl FROM PostLike pl JOIN pl.member m WHERE m.providerId = :providerId AND pl.post.id = :postCommentId")
    Optional<PostLike> findByProviderIdAndPostCommentId(@Param("providerId") String providerId, @Param("postCommentId") Long postCommentId);

    @Query("SELECT COUNT(pl) FROM PostLike pl WHERE pl.post.id = :postId")
    int countByPostId(@Param("postId") long postId);

}
