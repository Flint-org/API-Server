package com.flint.flint.club.controller.comment;

import com.flint.flint.club.request.ClubCommentCreateRequest;
import com.flint.flint.club.response.ClubCommentsAndRepliesResponse;
import com.flint.flint.club.service.comment.ClubCommentServiceImpl;
import com.flint.flint.common.ResponseForm;
import com.flint.flint.security.auth.dto.AuthorityMemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clubs")
public class ClubCommentController {
    private final ClubCommentServiceImpl clubCommentService;

    @PostMapping(path = "/{clubId}/comment")
    public void createComment(@AuthenticationPrincipal AuthorityMemberDTO memberDTO,
                              @RequestBody ClubCommentCreateRequest request,
                              @PathVariable("clubId") long clubId) {
        clubCommentService.createComment(memberDTO, request, clubId);
    }

    @PostMapping(path = "/{clubId}/reply")
    public void createCommentReply(@AuthenticationPrincipal AuthorityMemberDTO memberDTO,
                                   @RequestBody ClubCommentCreateRequest request,
                                   @PathVariable("clubId") long clubId,
                                   @RequestParam long parentCommentId) {
        clubCommentService.createCommentReply(memberDTO, request, clubId, parentCommentId);
    }

    @GetMapping(path = "/{clubId}/comment")
    public ResponseForm<List<ClubCommentsAndRepliesResponse>> getCommentsAndReplies(@PathVariable("clubId") Long clubId) {
        return new ResponseForm<>(clubCommentService.getComments(clubId));
    }

}
