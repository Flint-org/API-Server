package com.flint.flint.community.domain.post;

import com.flint.flint.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글 이미지 엔티티
 * @author 신승건
 * @since 2023-08-04
 */
@Entity
@Getter
@NoArgsConstructor
public class PostImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_IMAGE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID", nullable = false)
    private Post post;

    @Column(length = 100, nullable = false)
    private String imgUrl;

    @Builder
    public PostImage(Post post, String imgUrl) {
        this.post = post;
        this.imgUrl = imgUrl;
    }
}
