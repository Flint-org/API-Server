package com.flint.flint.community.controller;

import com.flint.flint.common.ResponseForm;
import com.flint.flint.community.dto.response.AllPostGetResponse;
import com.flint.flint.community.service.PostGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * 전체 게시판 검색
     */
    @GetMapping("/search/all/board")
    public ResponseForm<List<AllPostGetResponse>> searchInAllBoard(@RequestParam String keyword) {
        return new ResponseForm<>(postGetService.searchInAllBoard(keyword));
    }

    /**
     * 게시판 별 게시판 검색
     */
    @GetMapping("/search/specific/board")
    public ResponseForm<List<AllPostGetResponse>> searchInSpecificBoard(@RequestParam String boardName, @RequestParam String keyword) {
        return new ResponseForm<>(postGetService.searchInSpecificBoard(boardName, keyword));
    }
}
