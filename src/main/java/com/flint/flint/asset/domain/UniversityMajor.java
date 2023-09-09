package com.flint.flint.asset.domain;

import com.flint.flint.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 대학교 별 학과 정보 및 분류 정보 엔티티
 *
 * @author 신승건
 * @since 2023-08-30
 */
@Entity
@Table(indexes = {
        @Index(name = "idx_univ_name", columnList = "universityName"),
        @Index(name = "idx_univ_name_large_class", columnList = "universityName, largeClass")
})
@Getter
@NoArgsConstructor
public class UniversityMajor extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UNIVERSITY_MAJOR_ID")
    private Long id;

    @Column(nullable = false)
    private String universityName; // 대학교 명

    @Column(nullable = false)
    private String majorName; // 학부 또는 학과명

    @Column(nullable = false)
    private String largeClass; // 대분류

    @Column(nullable = false)
    private String mediumClass; // 중분류

    @Column(nullable = false)
    private String smallClass; // 소분류

    @Builder
    public UniversityMajor(String universityName, String majorName, String largeClass, String mediumClass, String smallClass) {
        this.universityName = universityName;
        this.majorName = majorName;
        this.largeClass = largeClass;
        this.mediumClass = mediumClass;
        this.smallClass = smallClass;
    }
}
