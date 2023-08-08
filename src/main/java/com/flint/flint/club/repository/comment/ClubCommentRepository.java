package com.flint.flint.club.repository.comment;

import com.flint.flint.club.domain.comment.ClubComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
/**
 * Club Comment Repository Interface Class
 * @author 김기현
 * @since 2023-08-03
 */
public interface ClubCommentRepository extends JpaRepository<ClubComment, UUID> {
}
