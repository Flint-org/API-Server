package com.flint.flint.asset.domain;

import com.flint.flint.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 대학교 정보 관련 데이터 보관용 엔티티
 *
 * @author 신승건
 * @since 2023-08-30
 */
@Entity
@Getter
@NoArgsConstructor
public class UniversityAsset extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UNIVERSITY_ASSET_ID")
    private Long id;

    @Column(nullable = false)
    private String universityName;

    @Column(nullable = false)
    private String logoUrl;

    @Column(nullable = false)
    private Integer red;

    @Column(nullable = false)
    private Integer green;

    @Column(nullable = false)
    private Integer blue;

    @Column(nullable = false)
    private String emailSuffix;

    @Builder
    public UniversityAsset(String universityName, String logoUrl, Integer red, Integer green, Integer blue, String emailSuffix) {
        this.universityName = universityName;
        this.logoUrl = logoUrl;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.emailSuffix = emailSuffix;
    }
}
