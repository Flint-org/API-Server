package com.flint.flint.club.domain.spec;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum ClubCategoryType {
    SERF_DEVELOPMENT("자기계발", null),
        STUDY("스터디", SERF_DEVELOPMENT),
        READING("독서", SERF_DEVELOPMENT),
        MIRACLE_MORNING("미라클모닝", SERF_DEVELOPMENT),
        STUDY_WITH_US("스터디윗어스", SERF_DEVELOPMENT),
        ETC("기타", SERF_DEVELOPMENT),

    // todo: 산책, 러닝, 헬스, 클라이밍, 배드민턴, 볼링, 축구, 풋살, 골프, 농구, 야구, 기타
    // 일단 산책 러닝 헬스만
    EXERCISE("운동", null),
        WALK("산책", EXERCISE),
        RUNNING("러닝", EXERCISE),
        HEALTH("헬스", EXERCISE);

    private final String categoryName;
    private final ClubCategoryType upperCategory;

    ClubCategoryType(String categoryName, ClubCategoryType upperCategory) {
        this.categoryName = categoryName;
        this.upperCategory = upperCategory;
    }
}
