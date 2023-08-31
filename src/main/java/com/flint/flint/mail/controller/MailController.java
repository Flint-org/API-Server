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
 * @Author 정순원
 * @Since 2023-08-31
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mail")
public class MailController {

    private final static Long Expiration = 1800000L; //한시간

    private final RedisUtil redisUtil;
    private final MailService mailService;
    private final MemberService memberService;
    private final AuthenticationService authenticationService;
    private final RateLimitService rateLimitService;


    /**
     * 인증번호 보내기
     * 검증: API호출 횟수
     */
    @PostMapping("/send")
    public ResponseForm<?> sendEmail(@RequestBody SendEmailAuthNumberReqeust request, @AuthenticationPrincipal AuthorityMemberDTO authorityMemberDTO) {
        Long key = authorityMemberDTO.getId();
        if(rateLimitService.checkAPICall(key)) { //API호출 횟수 검증
            int authNumber = mailService.sendCodeEmail(request.getEmail());
            redisUtil.saveAuthNumber(key, authNumber, Expiration);  //인증번호 검증을 위해 레디스에 저장
            EmailAuthNumberRespose response = new EmailAuthNumberRespose(authNumber);
            return new ResponseForm<>(response);
        }
        else { // API호출 한 시간에 10 번 이상 호출 한 경우 에러코드 보냄
            return new ResponseForm<>(USER_MANY_REQUEST);
        }
    }

    /**
     * 유저 권한 바꿔주고
     * 인증 한 유저 권한으로 새로운 액세스 토큰, 리프레쉬 토큰 발급해주기
     * 검증: 이메일 인증코드
     */
    @PostMapping("/success/auth")
    public ResponseForm<?> successUniversityAuth(@RequestBody VeriyEmailAuthnumberRequest request, @AuthenticationPrincipal AuthorityMemberDTO authorityMemberDTO) {
        Long key = authorityMemberDTO.getId();
        if(redisUtil.findEmailAuthNumberByKey(key) != request.getAuthNumber()){ //레디스에 저장한 인증번호와 다르다면 에러코드 보냄
            return new ResponseForm<>(MAIL_AUTHNUMBER_NOT);
        }
        Member member = memberService.getMember(authorityMemberDTO.getId());
        member.updateAuthority(Authority.AUTHUSER);
        AuthenticationResponse authenticationResponse = authenticationService.generateToken(member); //인증 한 유저권한을 담은 토큰 발급
        return new ResponseForm<>(authenticationResponse);
    }
}