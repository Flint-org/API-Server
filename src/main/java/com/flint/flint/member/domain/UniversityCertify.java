package com.flint.flint.member.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UniversityCertify {

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







