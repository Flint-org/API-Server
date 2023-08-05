package com.flint.flint.club.domain.main;

import com.flint.flint.club.domain.specification.ClubMeetingType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Club Entity Class
 * @author 김기현
 * @since 2023-08-03
 */
@Entity
@NoArgsConstructor
@Getter
public class Club {
    @Id @GeneratedValue(strategy = GenerationType.UUID) @Column(name = "club_id")
    private UUID id;
    @Column(name = "club_name")
    private String name;
    @Column(name = "club_description")
    private String description;
    @Column(name = "club_rule")
    private String rule;
    @Column(name = "club_view_count")
    private int viewCount;
    @Column(name = "club_start_date")
    private LocalDate meetingStartDate;
    @Column(name = "club_end_date")
    private LocalDate meetingEndDate;
    @Column(name = "club_meeting_type")
    private ClubMeetingType type;

    // 유저


    @Builder
    public Club(String name, String description, String rule, int viewCount, LocalDate meetingStartDate, LocalDate meetingEndDate, ClubMeetingType type) {
        this.name = name;
        this.description = description;
        this.rule = rule;
        this.viewCount = viewCount;
        this.meetingStartDate = meetingStartDate;
        this.meetingEndDate = meetingEndDate;
        this.type = type;
    }
}
