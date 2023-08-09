package com.flint.flint.club.domain.main;

import com.flint.flint.club.domain.specification.ClubFrequency;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Club Category Class : Online or Offline
 * @author 김기현
 * @since 2023-08-05
 */
@Entity
@Getter
@NoArgsConstructor
public class ClubCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "club_category_id")
    private Long id;
    @Column(name = "club_upper_category")
    private String upperCategory;
    @Column(name = "club_lower_category")
    private String lowerCategory;
    @Column(name = "club_frequency_type") @Enumerated(EnumType.STRING)
    private ClubFrequency frequencyType;

    @OneToOne @PrimaryKeyJoinColumn(name = "club_id")
    private Club club;

    @Builder
    public ClubCategory(String upperCategory, String lowerCategory, ClubFrequency frequencyType, Club club) {
        this.upperCategory = upperCategory;
        this.lowerCategory = lowerCategory;
        this.frequencyType = frequencyType;
        this.club = club;
    }
}
