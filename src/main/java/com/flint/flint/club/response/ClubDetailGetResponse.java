package com.flint.flint.club.response;

import com.flint.flint.club.domain.spec.*;
import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Data
public class ClubDetailGetResponse {
    // ClubCategory Entity
    private ClubCategoryType categoryType; // ClubCategory Entit
    private ClubFrequency frequencyType;// 일정 일회성, 주기성

    // ClubEntity
    private String name;
    private String description;
    private String rule; // 규칙
    private LocalDate meetingStartDate;
    private LocalDate meetingEndDate;
    private ClubMeetingType meetingType; // 온라인, 오프라인

    // ClubEnvironment Entity
    private String location; // Environment 내부 위치 설정
    private DayOfWeek day; // Environment 내부 날짜 설정

    // ClubRequirement Entity
    private ClubJoinRequirement joinType;// 승인 방식
    private String grade;
    private int memberLimitCount;
    private ClubGenderRequirement genderType;

    @Builder
    public ClubDetailGetResponse(ClubCategoryType categoryType,
                                 ClubFrequency frequencyType,
                                 String name,
                                 String description,
                                 String rule,
                                 LocalDate meetingStartDate,
                                 LocalDate meetingEndDate,
                                 ClubMeetingType meetingType,
                                 String location,
                                 DayOfWeek day,
                                 ClubJoinRequirement joinType,
                                 String grade,
                                 int memberLimitCount,
                                 ClubGenderRequirement genderType) {
        this.categoryType = categoryType;
        this.frequencyType = frequencyType;
        this.name = name;
        this.description = description;
        this.rule = rule;
        this.meetingStartDate = meetingStartDate;
        this.meetingEndDate = meetingEndDate;
        this.meetingType = meetingType;
        this.location = location;
        this.day = day;
        this.joinType = joinType;
        this.grade = grade;
        this.memberLimitCount = memberLimitCount;
        this.genderType = genderType;
    }
}
