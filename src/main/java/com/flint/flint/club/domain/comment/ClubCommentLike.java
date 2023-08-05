package com.flint.flint.club.domain.comment;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Club Comment Like Class
 * @author 김기현
 * @since 2023-08-05
 */
@Entity
@Data
@NoArgsConstructor
public class ClubCommentLike {
    @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(name = "club_comment_like_id")
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY) @Column(name = "club_comment_id")
    private ClubComment comment;

    // 유저

    @Builder
    public ClubCommentLike(ClubComment comment) {
        this.comment = comment;
    }
}
