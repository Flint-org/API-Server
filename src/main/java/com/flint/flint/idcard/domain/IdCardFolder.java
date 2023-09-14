package com.flint.flint.idcard.domain;

import com.flint.flint.common.BaseTimeEntity;
import com.flint.flint.member.domain.main.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 정순원
 * @since 2023-09-14
 */
@Entity
@Getter
@NoArgsConstructor
public class IdCardFolder extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_card_folder_id")
    private Long id;

    @Column(length = 50)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_card_id")
    private IdCard idcard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public IdCardFolder(String title, IdCard idcard, Member member) {
        this.title = title;
        this.idcard = idcard;
        this.member = member;
    }
}
