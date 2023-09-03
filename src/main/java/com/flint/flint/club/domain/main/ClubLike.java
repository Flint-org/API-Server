package com.flint.flint.club.domain.main;


import com.flint.flint.club.domain.main.Club;
import com.flint.flint.member.domain.main.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Club Like Class
 * @author 김기현
 * @since 2023-08-05
 */
@Entity
@Getter
@NoArgsConstructor
public class ClubLike {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "club_lke_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "club_id")
    private Club club;

    // 유저
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public ClubLike(Club club) {
        this.club = club;
    }
}
