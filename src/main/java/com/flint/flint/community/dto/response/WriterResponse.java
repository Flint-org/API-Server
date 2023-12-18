package com.flint.flint.community.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class WriterResponse {
    private Long memberId;
    private String universityLogoUrl;
    private String universityName;
    private String major;

    @QueryProjection
    public WriterResponse(Long memberId, String universityLogoUrl, String universityName, String major) {
        this.memberId = memberId;
        this.universityLogoUrl = universityLogoUrl;
        this.universityName = universityName;
        this.major = major;
    }
}
