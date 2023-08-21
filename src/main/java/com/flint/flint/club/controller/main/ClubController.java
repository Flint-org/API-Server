package com.flint.flint.club.controller.main;

import com.flint.flint.club.request.ClubCreateRequest;
import com.flint.flint.club.service.comment.ClubCommentServiceImpl;
import com.flint.flint.club.service.main.ClubServiceImpl;
import com.flint.flint.common.handler.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 모임을 위한 컨트롤러 클래스
 * @author 김기현
 * @since 23-08-10
 */
@RestController
@RequiredArgsConstructor
public class ClubController {
    private final ClubServiceImpl clubService;
    private final ClubCommentServiceImpl clubCommentService;

    @PostMapping(path = "/api/club")
    public void createClub(@RequestBody ClubCreateRequest clubCreateRequest) {
        clubService.createService(clubCreateRequest);
    }
}
