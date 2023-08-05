package com.flint.flint.member.domain.block;

import com.flint.flint.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ClubBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_block_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocker_id")
    private Member blocker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blockee_id")
    private Member blockee;

    @Builder
    public ClubBlock(Member blocker, Member blockee) {
        this.blocker = blocker;
        this.blockee = blockee;
    }
}
