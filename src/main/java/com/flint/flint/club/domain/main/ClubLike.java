package com.flint.flint.club.domain.main;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Club Like Class
 * @author 김기현
 * @since 2023-08-05
 */
@Entity
@Data
@NoArgsConstructor
public class ClubLike {
    @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(name = "club_id")
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Club club;

    // 유저

    @Builder
    public ClubLike(Club club) {
        this.club = club;
    }
}
