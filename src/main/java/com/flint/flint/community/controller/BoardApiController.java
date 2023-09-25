package com.flint.flint.community.controller;

import com.flint.flint.common.ResponseForm;
import com.flint.flint.community.dto.response.GeneralBoardResponse;
import com.flint.flint.community.dto.response.MajorBoardResponse;
import com.flint.flint.community.dto.response.UpperMajorInfoResponse;
import com.flint.flint.community.dto.response.UpperMajorListResponse;
import com.flint.flint.community.service.BoardService;
import com.flint.flint.security.auth.dto.AuthorityMemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 게시판 기능과 관련된 API 컨트롤러
 *
 * @author 신승건
 * @since 2023-08-21
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
public class BoardApiController {

    private final BoardService boardService;

    /**
     * 게시판 즐겨찾기 등록
     *
     * @param memberDTO 유저
     * @param boardId   즐겨찾기할 게시판 ID
     * @return x
     */
    @PreAuthorize("hasRole('ROLE_AUTHUSER')")
    @PostMapping("/{boardId}/bookmark")
    public ResponseForm<Void> bookmarkBoard(
            @AuthenticationPrincipal AuthorityMemberDTO memberDTO,
            @PathVariable("boardId") Long boardId
    ) {
        boardService.bookmarkBoard(memberDTO.getProviderId(), boardId);
        return new ResponseForm<>();
    }

    /**
     * 일반 게시판 목록 조회 API
     *
     * @return 전체 일반 게시판 목록
     */
    @GetMapping("/general")
    public ResponseForm<List<GeneralBoardResponse>> getGeneralBoardList() {
        return new ResponseForm<>(boardService.getGeneralBoardList());
    }

    /**
     * 전체 전공 게시판 목록 조회 API
     *
     * @return 전체 전공 게시판 목록
     */
    @GetMapping("/major")
    public ResponseForm<List<MajorBoardResponse>> getMajorBoardList() {
        return new ResponseForm<>(boardService.getHierarchyMajorBoardList());
    }

    /**
     * 대분류 전공 게시판 목록 조회 API
     *
     * @return 대분류 전공 게시판 목록만
     */
    @GetMapping("/major/uppers")
    public ResponseForm<List<UpperMajorListResponse>> getUpperMajorBoardList() {
        return new ResponseForm<>(boardService.getOnlyUpperMajorList());
    }

    /**
     * 특정 대분류의 소분류 전공 게시판 조회 API
     *
     * @param upperMajorId 조회하고자 하는 대분류 전공 게시판 ID
     * @return 특정 대분류에 대한 전체 소분류 전공 게시판 목록
     */
    @GetMapping("/major/uppers/{upperMajorId}")
    public ResponseForm<UpperMajorInfoResponse> getLowerMajorBoardList(
            @PathVariable("upperMajorId") Long upperMajorId
    ) {
        return new ResponseForm<>(boardService.getLowerMajorListByUpperMajor(upperMajorId));
    }
}
