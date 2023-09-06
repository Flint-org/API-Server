package com.flint.flint.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogoInfoResponse {
    private String universityName;
    private String logoUrl;
    private Integer red;
    private Integer green;
    private Integer blue;
}
