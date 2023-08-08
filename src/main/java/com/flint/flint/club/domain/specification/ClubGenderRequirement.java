package com.flint.flint.club.domain.specification;
/**
 * Club Gender Requirement Enum Class : None, Male, Female
 * @author 김기현
 * @since 2023-08-05
 */
public enum ClubGenderRequirement {
    NONE("None"),
    MALE("Male"),
    FEMALE("Female");

    private final String type;

    ClubGenderRequirement(String type) {
        this.type = type;
    }
}
