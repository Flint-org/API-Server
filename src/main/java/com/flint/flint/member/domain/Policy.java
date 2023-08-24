package com.flint.flint.member.domain;

import com.flint.flint.common.BaseTimeEntity;
import com.flint.flint.member.spec.Agree;
import com.flint.flint.security.auth.dto.RegisterRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 동의여부확인
 * @Author 정순원
 * @Since 2023-08-07
 */
@Entity
@Getter
@NoArgsConstructor
public class Policy extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policy_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memeber_id")
    private Member member;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Agree serviceUsingAgree;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Agree personalInformationAgree;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Agree marketingAgree;


    @Builder
    public Policy  (Member member, RegisterRequest registerRequest) {
        this.member = member;
        this.serviceUsingAgree = Agree.valueOf(registerRequest.getServiceUsingAgree());
        this.personalInformationAgree = Agree.valueOf(registerRequest.getPersonalInformationAgree());
        this.marketingAgree = Agree.valueOf(registerRequest.getMarketingAgree());
    }

}