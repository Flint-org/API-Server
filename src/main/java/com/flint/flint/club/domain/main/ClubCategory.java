package com.flint.flint.club.domain.main;

import com.flint.flint.club.domain.spec.ClubCategoryType;
import com.flint.flint.club.domain.spec.ClubFrequency;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Club Category Class : Online or Offline
 *
 * @author 김기현
 * @since 2023-08-05
 */
@Entity
@Getter
@NoArgsConstructor
public class ClubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_category_id")
    private Long id;
    @Column(name = "club_category")
    private ClubCategoryType categoryType;
    @Column(name = "club_frequency_type")
    @Enumerated(EnumType.STRING)
    private ClubFrequency frequencyType;

    @OneToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @Builder
    public ClubCategory(ClubCategoryType clubCategoryType,
                        ClubFrequency frequencyType,
                        Club club) {
        this.categoryType = clubCategoryType;
        this.frequencyType = frequencyType;
        this.club = club;
    }

    public void setClub(Club club) {
        this.club = club;
    }
}
