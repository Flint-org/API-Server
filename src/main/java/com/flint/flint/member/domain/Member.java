package com.flint.flint.member.domain;

import com.flint.flint.member.domain.spec.Authority;
import com.flint.flint.member.domain.spec.Gender;
import com.flint.flint.member.domain.spec.OAuthprovider;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/**
 * 첫 생성시 별점 0점과 유저권한으로 초기화
 * @Author 정순원
 * @Since 2023-08-07
 */
@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Email
    @NotNull
    @Column(unique = true)
    private String email;

    @Column(length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Max(5)
    @Min(0)
    @Column(name = "evaluation", columnDefinition = "FLOAT(10,2) CONSTRAINT chk_evaluation CHECK (evaluation BETWEEN 0 AND 5)")
    private Float evaluation = (float) 0;

    //유저, 관리자
    @Enumerated(EnumType.STRING)
    private Authority authority = Authority.USER;
    ;

    @Enumerated(EnumType.STRING)
    @Column(name = "OAuthProvider")
    private OAuthprovider oauthProvider;

    @Builder
    public Member(String email,
                  String name,
                  Gender gender,
                  LocalDate birthday,
                  OAuthprovider oauthProvider) {
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.oauthProvider = oauthProvider;
    }

}
