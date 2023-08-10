package com.flint.flint.community.domain;

import com.flint.flint.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글 엔티티
 * @author 신승건
 * @since 2023-08-04
 */
@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long id;

    // 유저

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_CATEGORY_ID")
    private BoardCategory boardCategory;

    @Column(length = 30, nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String contents;

    @Builder
    public Post(BoardCategory boardCategory, String title, String contents) {
        this.boardCategory = boardCategory;
        this.title = title;
        this.contents = contents;
    }
}
