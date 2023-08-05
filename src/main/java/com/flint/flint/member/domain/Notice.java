package com.flint.flint.member.domain;

import com.flint.flint.member.domain.enums.NoticeType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memeber_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private NoticeType noticeType;


    @Builder
    public Notice(Member member, NoticeType noticeType) {
        this.member = member;
        this.noticeType = noticeType;
    }
}
