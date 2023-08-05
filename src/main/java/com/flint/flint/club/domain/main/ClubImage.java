package com.flint.flint.club.domain.main;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Club Image Class
 * @author 김기현
 * @since 2023-08-05
 */
@Entity
@Data
@NoArgsConstructor
public class ClubImage {
    @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(name = "club_image_id")
    private UUID id;
    @Column(name = "club_image_url")
    private String imgUrl;

    @OneToOne @Column(name = "club_id")
    private Club club;

    @Builder
    public ClubImage(String imgUrl, Club club) {
        this.imgUrl = imgUrl;
        this.club = club;
    }
}
