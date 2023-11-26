package com.flint.flint.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WriterResponse {
    private Long memberId;
    private String universityLogoUrl;
    private String universityName;
    private String major;
}
