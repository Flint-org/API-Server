package com.flint.flint.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostPreSignedUrlResponse {
    private String fileName;
    private String preSignedUrl;
}
