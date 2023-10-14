package com.flint.flint.community.repository;

import com.flint.flint.community.domain.post.PostCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author 정순원
 * @since 2023-10-11
 */
public interface PostCommentLikeRepository extends JpaRepository<PostCommentLike, Long> {

    @Query("SELECT pcl FROM PostCommentLike pcl JOIN pcl.member m WHERE m.providerId = :providerId AND pcl.postComment.id = :postCommentId")
    Optional<PostCommentLike> findByProviderIdAndPostCommentId(@Param("providerId") String providerId, @Param("postCommentId") Long postCommentId);
}
