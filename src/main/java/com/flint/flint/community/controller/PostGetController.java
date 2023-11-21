package com.flint.flint.community.controller;

import com.flint.flint.common.ResponseForm;
import com.flint.flint.community.dto.response.DetailPostResponse;
import com.flint.flint.community.dto.response.PostResponse;
import com.flint.flint.community.service.PostGetService;
import com.flint.flint.security.auth.dto.AuthorityMemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 정순원
 * @since 2023-10-18
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostGetController {

    private final PostGetService postGetService;

    /**
     * 전체 게시판 내에
     * 게시물 검색
     */
    @GetMapping("/search/all/board")
    public ResponseForm<List<PostResponse>> searchInAllBoard(@RequestParam String keyword,
                                                             @PageableDefault(size = 10) Pageable pageable) {
        return new ResponseForm<>(postGetService.searchInAllBoard(keyword, pageable));
    }

    /**
     * 해당하는 게시판 내에
     * 게시물 검색
     */
    @GetMapping("/search/specific/board")
    public ResponseForm<List<PostResponse>> searchInSpecificBoard(@RequestParam String boardName, @RequestParam String keyword,
                                                                  @PageableDefault(size = 10) Pageable pageable) {
        return new ResponseForm<>(postGetService.searchInSpecificBoard(boardName, keyword, pageable));
    }

    /**
     * 게시물 상세 조회
     *
     * @param memberDTO 조회 유저
     * @param postId    조회할 게시글 ID
     * @return 게시글 관련 모든 메타데이터 및 데이터 정보
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{postId}")
    public ResponseForm<DetailPostResponse> getDetailPost(
            @AuthenticationPrincipal AuthorityMemberDTO memberDTO,
            @PathVariable("postId") Long postId
    ) {
        return new ResponseForm<>(postGetService.getDetailPost(postId));
    }
}
