package com.flint.flint.member.domain;

import com.flint.flint.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자의 대학인증관련자료를 저장하는 객체
 * @Author 정순원
 * @Since 2023-08-07
 */
@Entity
@Getter
@NoArgsConstructor
public class UniversityCertify extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "university_certify_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 100)
    private String university;

    @Column(length = 100)
    private String major;

    @Column(length = 500)
    private String certifyImage;

    @Builder
    public UniversityCertify(Member memeber,
                             String university,
                             String major,
                             String certifyImage) {
        this.member = memeber;
        this.university = university;
        this.major = major;
        this.certifyImage = certifyImage;
    }

}