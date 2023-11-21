package com.flint.flint.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailPostResponse {
    private Long postId;
    private String boardName;
    private WriterResponse writer;
    private String title;
    private String contents;
    private LocalDateTime createAt;
    private int likeCount;
    private int scrapCount;
    private int commentCount;
    private List<String> images;
}
