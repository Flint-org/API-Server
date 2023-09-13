package com.flint.flint.idcard.domain;

import com.flint.flint.club.domain.main.Club;
import com.flint.flint.common.BaseTimeEntity;
import com.flint.flint.member.domain.main.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Length;

import java.util.List;

/**
 * @Author 정순원
 * @Since 2023-08-07
 */
@Entity
@Getter
@NoArgsConstructor
public class IdCardBox extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_card_box_id")
    private Long id;

    @Column(length = 100)
    private String folder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_card_id")
    private IdCard idcard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public IdCardBox(IdCard idcard, Member member) {
        this.idcard = idcard;
        this.member = member;
    }

    public void updateFolder(String folder) {
        this.folder = folder;
    }

}
