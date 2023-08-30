package com.flint.flint.club.domain.main;

import com.flint.flint.club.domain.spec.MemberJoinStatus;
import com.flint.flint.member.domain.main.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MemberInClub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @Enumerated(EnumType.STRING)
    @Column(name = "member_join_status")
    private MemberJoinStatus status;

    @Builder
    public MemberInClub(Club club, Member member, MemberJoinStatus status) {
        this.club = club;
        this.member = member;
        this.status = status;
    }
}
