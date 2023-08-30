package com.flint.flint.club.domain.comment;

import com.flint.flint.member.domain.main.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Club Comment Like Class
 *
 * @author 김기현
 * @since 2023-08-05
 */
@Entity
@Getter
@NoArgsConstructor
public class ClubCommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "club_comment_like_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_comment_id")
    private ClubComment comment;

    // 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public ClubCommentLike(ClubComment comment) {
        this.comment = comment;
    }
}
