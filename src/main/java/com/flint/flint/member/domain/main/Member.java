package com.flint.flint.member.domain.main;

import com.flint.flint.common.BaseTimeEntity;
import com.flint.flint.member.spec.Authority;
import com.flint.flint.member.spec.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 첫 생성시 별점 0점, 인증 안한 유저로 초기화
 *
 * @Author 정순원
 * @Since 2023-08-07
 */
@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Email
    @NotNull
    private String email;

    @Column(length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(length = 4)
    private String birthday;

    @Max(5)
    @Min(0)
    private Float evaluation = (float) 0;

    //유저, 관리자
    @Enumerated(EnumType.STRING)
    private Authority authority = Authority.UNAUTHUSER;

    @NotNull
    private String providerName;

    @NotNull
    private String providerId;

    @Builder
    public Member(String email,
                  String name,
                  Gender gender,
                  String birthday,
                  Authority authority,
                  String providerName,
                  String providerId) {
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.authority = authority;
        this.providerName = providerName;
        this.providerId = providerId;
    }

    public Member updateAuthority(Authority authority) {
        this.authority = authority;
        return this;
    }
}
