package com.flint.flint.community.controller;

import com.flint.flint.common.ResponseForm;
import com.flint.flint.community.dto.response.PostCommentGetResponse;
import com.flint.flint.community.service.PostCommentGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 정순원
 * @since 2023-10-09
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/comment")
@PreAuthorize("hasRole('ROLE_UNAUTHUSER')")
public class PostCommentGetController {

    private final PostCommentGetService postCommentGeteService;

    /**
     * 특정 포스트 댓글/대댓글 조회
     */
    @GetMapping("/{postId}")
    public ResponseForm<List<PostCommentGetResponse>> getPostComment(@PathVariable long postId) {
        return new ResponseForm<>(postCommentGeteService.getPostComment(postId));
    }
}