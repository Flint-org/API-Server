package com.flint.flint.community.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 일반 게시판 목록 조회 결과 DTO
 * @author 신승건
 * @since 2023-08-21
 */
@Data
@NoArgsConstructor
public class GeneralBoardResponse {

    private Long boardId;
    private String boardName;

    @Builder
    public GeneralBoardResponse(Long boardId, String boardName) {
        this.boardId = boardId;
        this.boardName = boardName;
    }
}
