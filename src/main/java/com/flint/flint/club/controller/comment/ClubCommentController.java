package com.flint.flint.club.controller.comment;

import com.flint.flint.club.request.ClubCommentCreateRequest;
import com.flint.flint.club.service.comment.ClubCommentServiceImpl;
import com.flint.flint.security.auth.dto.AuthorityMemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ClubCommentController {
    private final ClubCommentServiceImpl clubCommentService;

    @PostMapping(path = "/api/v1/club/{id}/comment")
    public void createComment(@AuthenticationPrincipal AuthorityMemberDTO memberDTO,
                              @RequestBody ClubCommentCreateRequest request,
                              @PathVariable("id") long clubId) {
        clubCommentService.createComment(memberDTO, request, clubId);
    }

    @PostMapping(path = "/api/v1/club/{id}/reply")
    public void createCommentReply(@AuthenticationPrincipal AuthorityMemberDTO memberDTO,
                                   @RequestBody ClubCommentCreateRequest request,
                                   @PathVariable("id") long clubId,
                                   @RequestParam long parentCommentId) {
        clubCommentService.createCommentReply(memberDTO, request, clubId, parentCommentId);
    }
}
