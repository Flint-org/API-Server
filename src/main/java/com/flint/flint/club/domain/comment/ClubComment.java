package com.flint.flint.club.domain.comment;

import com.flint.flint.club.domain.main.Club;
import com.flint.flint.member.domain.main.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Club Comment Entity Class
 * @author 김기현
 * @since 2023-08-03
 */
@Entity
@NoArgsConstructor
@Getter
public class ClubComment {
    @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(name = "club_comment_id")
    private UUID id;
    @Column(name = "club_comment_contents")
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "club_id")
    private Club club;

    // comment 순환 참조

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "club_comment_parent_id")
    private ClubComment commentParent;

    @OneToMany(mappedBy = "commentParent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClubComment> commentChildren;

    // 유저
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public ClubComment(String contents, Club club) {
        this.contents = contents;
        this.club = club;
    }
}
