package com.flint.flint.community.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 전공 게시판 전체 조회 결과 DTO
 * @author 신승건
 * @since 2023-08-21
 */
@Data
@NoArgsConstructor
public class MajorBoardResponse {
    private Long boardId;
    private UpperMajorInfoResponse upperMajor;

    @Builder
    public MajorBoardResponse(Long boardId, UpperMajorInfoResponse upperMajor) {
        this.boardId = boardId;
        this.upperMajor = upperMajor;
    }
}
