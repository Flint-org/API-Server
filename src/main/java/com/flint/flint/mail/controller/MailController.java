package com.flint.flint.mail.controller;

import com.flint.flint.common.ResponseForm;
import com.flint.flint.mail.dto.request.SendEmailAuthNumberReqeust;
import com.flint.flint.mail.dto.request.VeriyEmailAuthnumberRequest;
import com.flint.flint.mail.dto.response.EmailAuthNumberRespose;
import com.flint.flint.mail.service.MailService;
import com.flint.flint.mail.service.RateLimitService;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.domain.spec.Authority;
import com.flint.flint.member.service.MemberService;
import com.flint.flint.redis.RedisUtil;
import com.flint.flint.security.auth.AuthenticationService;
import com.flint.flint.security.auth.dto.AuthenticationResponse;
import com.flint.flint.security.auth.dto.AuthorityMemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.flint.flint.common.spec.ResultCode.MAIL_AUTHNUMBER_NOT;
import static com.flint.flint.common.spec.ResultCode.USER_MANY_REQUEST;

/**
 * 메일 발송 관련 API
 *
 * @Author 정순원
 * @Since 2023-08-31
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mail")
public class MailController {


    private final MailService mailService;
    /**
     * 인증번호 보내기
     * 검증: API호출 횟수
     */
    @PostMapping("/send")
    public ResponseForm<EmailAuthNumberRespose> sendEmail(@RequestBody SendEmailAuthNumberReqeust request, @AuthenticationPrincipal AuthorityMemberDTO authorityMemberDTO) {
        Long key = authorityMemberDTO.getId();
        return new ResponseForm<>(mailService.sendCodeEmail(request.getEmail(), key));
    }

    /**
     * 유저 권한 바꿔주고
     * 인증 한 유저 권한으로 새로운 액세스 토큰, 리프레쉬 토큰 발급해주기
     * 검증: 이메일 인증코드
     */
    @PostMapping("/success/auth")
    public ResponseForm<AuthenticationResponse> successUniversityAuth(@RequestBody VeriyEmailAuthnumberRequest request, @AuthenticationPrincipal AuthorityMemberDTO authorityMemberDTO) {
        Long key = authorityMemberDTO.getId();
        return new ResponseForm<>(mailService.successEmailAuth(request, key));
    }
}