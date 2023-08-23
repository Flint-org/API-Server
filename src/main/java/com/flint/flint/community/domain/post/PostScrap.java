package com.flint.flint.community.domain.post;

import com.flint.flint.common.BaseTimeEntity;
import com.flint.flint.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글 스크랩 엔티티
 * @author 신승건
 * @since 2023-08-04
 */
@Entity
@Getter
@NoArgsConstructor
public class PostScrap extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_SCRAP_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public PostScrap(Post post, Member member) {
        this.post = post;
        this.member = member;
    }
}
