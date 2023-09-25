package com.flint.flint.club.controller.main;

import com.flint.flint.club.domain.main.Club;
import com.flint.flint.club.domain.spec.ClubCategoryType;
import com.flint.flint.club.request.ClubCreateRequest;
import com.flint.flint.club.response.ClubDetailGetResponse;
import com.flint.flint.club.service.comment.ClubCommentServiceImpl;
import com.flint.flint.club.service.main.ClubServiceImpl;
import com.flint.flint.common.ResponseForm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 모임을 위한 컨트롤러 클래스
 * @author 김기현
 * @since 23-08-10
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clubs")
public class ClubController {
    private final ClubServiceImpl clubService;
    private final ClubCommentServiceImpl clubCommentService;

    @PreAuthorize("hasRole('ROLE_AUTHUSER')")
    @PostMapping(path = "")
    public void createClub(@RequestBody ClubCreateRequest clubCreateRequest) {
        clubService.createClub(clubCreateRequest);
    }

    @GetMapping(path = "")
    public ResponseForm<Page<Club>> getClubs(@RequestParam ClubCategoryType clubCategoryType,
                                            @RequestParam String sortProperties,
                                            @RequestParam String direction) {
        return new ResponseForm<>(clubService.getClubs(clubCategoryType, sortProperties, direction));
    }

    @GetMapping(path = "{clubId}")
    public ResponseForm<ClubDetailGetResponse> getClubDetail(@PathVariable("clubId") Long clubId,
                                               @RequestParam ClubCategoryType clubCategoryType) {
        return new ResponseForm<>(clubService.getClubDetail(clubId, clubCategoryType));
    }
}
