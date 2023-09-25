package com.flint.flint.community.controller;

import com.flint.flint.common.ResponseForm;
import com.flint.flint.community.dto.request.PostRequest;
import com.flint.flint.community.dto.response.PostPreSignedUrlResponse;
import com.flint.flint.community.service.PostService;
import com.flint.flint.security.auth.dto.AuthorityMemberDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 신승건
 * @since 2023-09-13
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostApiController {
    private final PostService postService;

    @PreAuthorize("hasRole('ROLE_AUTHUSER')")
    @PostMapping("")
    public ResponseForm<List<PostPreSignedUrlResponse>> createPost(
            @AuthenticationPrincipal AuthorityMemberDTO memberDTO,
            @Valid @RequestBody PostRequest postRequest
    ) {
        return new ResponseForm<>(postService.createPost(memberDTO.getProviderId(), postRequest));
    }
}
