package com.flint.flint.club.request;

import com.flint.flint.club.domain.main.Club;
import com.flint.flint.club.domain.spec.ClubMeetingType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ClubCreateRequest {
    private String name;
    private String description;
    private String rule;
    private LocalDate meetingStartDate;
    private LocalDate meetingEndDate;
    private ClubMeetingType type;

    @Builder
    public ClubCreateRequest(String name, String description, String rule, LocalDate meetingEndDate, LocalDate meetingStartDate, ClubMeetingType type) {
        this.name = name;
        this.description  = description;
        this.rule = rule;
        this.meetingEndDate = meetingEndDate;
        this.meetingStartDate = meetingStartDate;
        this.type = type;
    }

    /**
     * 모임 DTO를 Entity로 전환하는 메소드
     */
    public Club toEntity() {
        return Club.builder()
                .name(name)
                .description(description)
                .rule(rule)
                .meetingStartDate(meetingStartDate)
                .meetingEndDate(meetingEndDate)
                .type(type).build();
    }
}
