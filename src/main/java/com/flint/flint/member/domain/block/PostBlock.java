package com.flint.flint.member.domain.block;

import com.flint.flint.member.domain.Member;
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
public class PostBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_block_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocker_id")
    private Member blocker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blockee_id")
    private Member blockee;

    @Builder
    public PostBlock(Member blocker, Member blockee) {
        this.blocker = blocker;
        this.blockee = blockee;
    }

}
