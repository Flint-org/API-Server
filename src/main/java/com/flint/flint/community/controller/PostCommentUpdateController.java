package com.flint.flint.community.controller;

import com.flint.flint.common.ResponseForm;
import com.flint.flint.community.dto.request.PostCommentUpdateRequest;
import com.flint.flint.community.dto.response.PostCommentUpdateResponse;
import com.flint.flint.community.service.PostCommentLikeUpdateService;
import com.flint.flint.community.service.PostCommentUpdateService;
import com.flint.flint.security.auth.dto.AuthorityMemberDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * @author 정순원
 * @since 2023-09-30
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/comment")
@PreAuthorize("hasRole('ROLE_AUTHUSER')")
public class PostCommentUpdateController {

    private final PostCommentUpdateService postCommentUpdateService;
    private final PostCommentLikeUpdateService postCommentLikeUpdateService;

    @PostMapping("/{postId}")
    public ResponseForm<PostCommentUpdateResponse> createPostComment(
            @AuthenticationPrincipal AuthorityMemberDTO memberDTO, @PathVariable long postId,
            @Valid @RequestBody PostCommentUpdateRequest requestDTO) {
        return new ResponseForm<>(postCommentUpdateService.createPostComment(memberDTO.getProviderId(), postId, requestDTO));
    }
    @DeleteMapping("/{postCommentId}")
    public ResponseForm deletePostComment(@AuthenticationPrincipal AuthorityMemberDTO memberDTO, @PathVariable long postCommentId) {
        postCommentUpdateService.deletePostComment(memberDTO.getProviderId(), postCommentId);
        return new ResponseForm<>();
    }

    @PutMapping("/{postCommentId}")
    public ResponseForm updatePostComment(@AuthenticationPrincipal AuthorityMemberDTO memberDTO, @PathVariable long postCommentId,
                                          @Valid @RequestBody PostCommentUpdateRequest request) {
        postCommentUpdateService.updatePostComment(memberDTO.getProviderId(), postCommentId, request);
        return new ResponseForm<>();
    }

    @PostMapping("/{postCommentId}")
    public ResponseForm createPostCommentLike (@AuthenticationPrincipal AuthorityMemberDTO memberDTO, @PathVariable long postCommentId) {
        postCommentLikeUpdateService.createPostCommentLike(memberDTO.getProviderId(), postCommentId);
        return new ResponseForm<>();
    }
}

