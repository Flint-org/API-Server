package com.flint.flint.club.domain.main;

import com.flint.flint.club.domain.spec.ClubMeetingType;
import com.flint.flint.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Club Entity Class
 *
 * @author 김기현
 * @since 2023-08-03
 */
@Entity
@NoArgsConstructor
@Getter
public class Club extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long id;
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
    @Enumerated(EnumType.STRING)
    private ClubMeetingType meetingType;

    @Builder
    public Club(String name,
                String description,
                String rule,
                int viewCount,
                LocalDate meetingStartDate,
                LocalDate meetingEndDate,
                ClubMeetingType meetingType) {
        this.name = name;
        this.description = description;
        this.rule = rule;
        this.viewCount = viewCount;
        this.meetingStartDate = meetingStartDate;
        this.meetingEndDate = meetingEndDate;
        this.meetingType = meetingType;
    }

}
