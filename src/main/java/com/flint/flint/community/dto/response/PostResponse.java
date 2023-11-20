package com.flint.flint.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostResponse {

    private long postId;
    private String title;
    private String imageURL;
}
