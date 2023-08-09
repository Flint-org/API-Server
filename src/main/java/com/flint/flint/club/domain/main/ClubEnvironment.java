package com.flint.flint.club.domain.main;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Club Environment Class such as Meeting Date or Location
 * @author 김기현
 * @since 2023-08-05
 */
@Entity
@Getter
@NoArgsConstructor
public class ClubEnvironment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "club_environment_id")
    private Long id;
    @Column(name = "club_location")
    private String location;
    @Column(name = "club_date_of_week")
    private LocalDate day;

    @OneToOne @PrimaryKeyJoinColumn(name = "club_id")
    private Club club;

    @Builder
    public ClubEnvironment(String location, LocalDate day, Club club) {
        this.location = location;
        this.day = day;
        this.club = club;
    }
}
