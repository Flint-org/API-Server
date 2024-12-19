package com.flint.flint.community.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Writer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentResponse {

    private Long parentCommentId;
    private long postCommentId;
    private String contents;
    private LocalDateTime createdAt;
    private long memberId;
    private Long likeCount;
    private String logoUrl;
    private WriterResponse writerResponse;
    private List<PostCommentResponse> replies = new ArrayList<>();

    @QueryProjection
    public PostCommentResponse(Long parentCommentId, long postCommentId, String contents, LocalDateTime createdAt, long memberId, Long likeCount, String logoUrl) {
        this.parentCommentId = parentCommentId;
        this.postCommentId = postCommentId;
        this.contents = contents;
        this.createdAt = createdAt;
        this.memberId = memberId;
        this.likeCount = likeCount;
        this.logoUrl = logoUrl;
    }
}
