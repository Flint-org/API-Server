package com.flint.flint.club.repository.comment;

import com.flint.flint.club.domain.comment.ClubComment;
import com.flint.flint.club.domain.main.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
/**
 * Club Comment Repository Interface Class
 * @author 김기현
 * @since 2023-08-03
 */
public interface ClubCommentRepository extends JpaRepository<ClubComment, Long> {
    Optional<List<ClubComment>> findByClub(Club club);
    @Query(value = "SELECT c FROM ClubComment c WHERE c.commentParent = null and c.club = :club")
    Optional<List<ClubComment>> findAllByCommentParent(@Param("club") Club club);
}
