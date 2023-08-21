package com.flint.flint.club.request;

import com.flint.flint.club.domain.main.Club;
import com.flint.flint.club.domain.main.ClubCategory;
import com.flint.flint.club.domain.main.ClubEnvironment;
import com.flint.flint.club.domain.main.ClubRequirement;
import com.flint.flint.club.domain.spec.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ClubCreateRequest {
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


    // todo : 이미지, 해시태그
    @Builder
    public ClubCreateRequest(ClubCategoryType categoryType,
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

    /**
     * 모임 DTO를 Entity로 전환하는 메소드
     * Club, ClubEnvironment, ClubCategory, ClubRequirement
     */
    // Club
    public Club toClubEntity() {
        return Club.builder()
                .name(this.name)
                .description(this.description)
                .rule(this.rule)
                .meetingStartDate(this.meetingStartDate)
                .meetingEndDate(this.meetingEndDate)
                .meetingType(this.meetingType)
                .build();
    }
    // Environment
    public ClubEnvironment toClubEnvironmentEntity(Club getClubInRepository) {
        return ClubEnvironment.builder()
                .location(this.location)
                .day(this.day)
                .club(getClubInRepository)
                .build();
    }
    // Requirement
    public ClubRequirement toClubRequirementEntity(Club getClubInRepository) {
        return ClubRequirement.builder()
                .joinType(this.joinType)
                .grade(this.grade)
                .memberLimitCount(this.memberLimitCount)
                .genderType(this.genderType)
                .club(getClubInRepository)
                .build();
    }
    // Category
    public ClubCategory toClubCategoryEntity(Club getClubInRepository) {
        return ClubCategory.builder()
                .clubCategoryType(this.categoryType)
                .frequencyType(this.frequencyType)
                .club(getClubInRepository)
                .build();
    }
}
