package com.flint.flint.member.domain.idCard;

import com.flint.flint.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class IdCard {

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
