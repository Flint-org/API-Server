package com.flint.flint.member.domain.idcard;

import com.flint.flint.common.BaseTimeEntity;
import com.flint.flint.member.domain.main.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author 정순원
 * @Since 2023-08-07
 */
@Entity
@Getter
@NoArgsConstructor
public class IdCard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_card_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 500, columnDefinition = "longtext")
    private String cardBackIntroduction;

    @Column(length = 500, columnDefinition = "longtext")
    private String cardBackExpercience;

    @Column(length = 100)
    private String cardFrontSNSId;

    @Builder
    public IdCard(Member member,
                  String cardBackIntroduction,
                  String cardBackExpercience,
                  String cardFrontSNSId){
        this.member = member;
        this.cardBackIntroduction = cardBackIntroduction;
        this.cardBackExpercience = cardBackExpercience;
        this.cardFrontSNSId = cardFrontSNSId;
    }

}
