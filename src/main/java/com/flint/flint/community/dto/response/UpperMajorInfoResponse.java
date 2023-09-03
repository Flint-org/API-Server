package com.flint.flint.community.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 특정 전공 게시판 정보 조회 결과 DTO
 * @author 신승건
 * @since 2023-08-21
 */
@Data
@NoArgsConstructor
public class UpperMajorInfoResponse {
    private Long upperMajorId;
    private String upperMajorName;
    private List<LowerMajorBoardResponse> lowerMajors;

    @Builder
    public UpperMajorInfoResponse(Long upperMajorId, String upperMajorName, List<LowerMajorBoardResponse> lowerMajors) {
        this.upperMajorId = upperMajorId;
        this.upperMajorName = upperMajorName;
        this.lowerMajors = lowerMajors;
    }
}
