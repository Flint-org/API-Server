package com.flint.flint.community.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 소분류 전공 게시판 조회 결과 DTO
 *
 * @author 신승건
 * @since 2023-08-21
 */
@Data
@NoArgsConstructor
public class LowerMajorBoardResponse {
    private Long boardId;
    private String lowerMajorName;

    @Builder
    public LowerMajorBoardResponse(Long boardId, String lowerMajorName) {
        this.boardId = boardId;
        this.lowerMajorName = lowerMajorName;
    }
}
