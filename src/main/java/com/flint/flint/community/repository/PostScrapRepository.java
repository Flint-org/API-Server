package com.flint.flint.community.repository;

import com.flint.flint.community.domain.post.PostScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostScrapRepository extends JpaRepository<PostScrap, Long> {

    @Query("SELECT ps FROM PostScrap ps JOIN ps.member m WHERE m.providerId = :providerId AND ps.post.id = :postId")
    Optional<PostScrap> findByProviderIdAndPostId(@Param("providerId") String providerId, @Param("postId") Long postId);

}
