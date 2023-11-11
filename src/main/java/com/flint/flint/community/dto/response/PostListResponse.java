package com.flint.flint.community.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostListResponse {
    private Long postId;
    private String title;
    private String thumbnailImage;
    private int commentCount;
    private int likeCount;
    private int scrapCount;
    private LocalDateTime createdAt;

    @QueryProjection
    @Builder
    public PostListResponse(Long postId, String title, String thumbnailImage, int commentCount, int likeCount, int scrapCount, LocalDateTime createdAt) {
        this.postId = postId;
        this.title = title;
        this.thumbnailImage = thumbnailImage;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.scrapCount = scrapCount;
        this.createdAt = createdAt;
    }
}
