package com.flint.flint.club.repository.comment;

import com.flint.flint.club.domain.comment.ClubComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClubCommentRepository extends JpaRepository<ClubComment, UUID> {
}
