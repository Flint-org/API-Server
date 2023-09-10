package com.flint.flint.club.domain.main;

import com.flint.flint.club.domain.spec.ClubGenderRequirement;
import com.flint.flint.club.domain.spec.ClubJoinRequirement;
import com.flint.flint.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Club Join Requirement Class
 * 1. 승인 방식
 * 2. 학점 조건
 * 3. 인원 조건
 * 4. 성별 조건
 * 5. 기타 조건
 *
 * @author 김기현
 * @since 2023-08-05
 */
@Entity
@Getter
@NoArgsConstructor
public class ClubRequirement extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_Requirement_id")
    private Long id;
    @Column(name = "join_authorize_type")
    @Enumerated(EnumType.STRING)
    private ClubJoinRequirement joinType;
    @Column(name = "club_grade_limit")
    private String grade;
    @Column(name = "club_member_limit")
    private int memberLimitCount;
    @Column(name = "club_gender_type")
    @Enumerated(EnumType.STRING)
    private ClubGenderRequirement genderType;
    @Column(name = "club_etc_limit")
    private String etc;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "club_id")
    private Club club;

    @Builder
    public ClubRequirement(
            ClubJoinRequirement joinType,
            String grade,
            int memberLimitCount,
            ClubGenderRequirement genderType,
            String etc,
            Club club) {
        this.joinType = joinType;
        this.grade = grade;
        this.memberLimitCount = memberLimitCount;
        this.genderType = genderType;
        this.etc = etc;
        this.club = club;
    }

    public void setClub(Club club) {
        this.club = club;
    }
}
