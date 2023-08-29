package com.flint.flint.member.controller;

import com.flint.flint.common.ResponseForm;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.domain.spec.Authority;
import com.flint.flint.member.domain.spec.Gender;
import com.flint.flint.member.dto.response.SendEmailResponse;
import com.flint.flint.member.service.MailService;
import com.flint.flint.member.service.MemberService;
import com.flint.flint.security.auth.AuthenticationService;
import com.flint.flint.security.auth.dto.AuthenticationResponse;
import com.flint.flint.security.auth.dto.AuthorityMemberDTO;
import com.flint.flint.security.auth.dto.ClaimsDTO;
import com.flint.flint.security.auth.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 메일 발송 관련 API
 * @Author 정순원
 * @Since 2023-08-07
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailController {

    private final MailService mailService;
    private final MemberService memberService;
    private final AuthenticationService authenticationService;

    /**
     * 인증번호 보내기
     */
    @GetMapping("/send/{email}")
    public ResponseForm<SendEmailResponse> sendEmail(@PathVariable("email") String email) {
        int authNumber = mailService.sendCodeEmail(email);
        SendEmailResponse sendEmailResponse = new SendEmailResponse(authNumber);
        return new ResponseForm<>(sendEmailResponse);
    }

    /**
     * 유저 권한 바꿔주고,
     * 인증 한 유저 권한으로 새로운 액세스 토큰, 리프레쉬 토큰 발급해주기
     */
    @PostMapping("/success/auth")
    public ResponseForm<AuthenticationResponse> successUniversityAuth(@AuthenticationPrincipal AuthorityMemberDTO memberDTO) {
        Member member = memberService.getMember(memberDTO.getId());
        member.updateAuthority(Authority.AUTHUSER);
        AuthenticationResponse authenticationResponse = authenticationService.generateToken(member);
        return new ResponseForm<>(authenticationResponse);
    }
}