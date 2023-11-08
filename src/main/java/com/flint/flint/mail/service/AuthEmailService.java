package com.flint.flint.mail.service;

import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.common.spec.ResultCode;
import com.flint.flint.idcard.service.IdCardService;
import com.flint.flint.mail.dto.request.SuccessUniversityAuthRequest;
import com.flint.flint.mail.dto.response.EmailAuthNumberRespose;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.service.MemberService;
import com.flint.flint.member.spec.Authority;
import com.flint.flint.redis.RedisUtil;
import com.flint.flint.security.auth.AuthenticationService;
import com.flint.flint.security.auth.dto.AuthenticationResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @Author 정순원
 * @Since 2023-08-31
 */
@Service
@RequiredArgsConstructor
public class AuthEmailService {

    private static final Long EXPIRATION = 1800000L; //한시간
    private int authNumber;

    private final RandomGenerator randomGenerator;
    private final RateLimitService rateLimitService;
    private final SendMailService sendMailService;

    private final RedisUtil redisUtil;
    private final MemberService memberService;
    private final AuthenticationService authenticationService;
    private final IdCardService idCardService;

    @Transactional
    public EmailAuthNumberRespose sendCodeEmail(String email, Long key) {
        if (rateLimitService.checkAPICall(key)) { //API호출 횟수 검사
            authNumber = randomGenerator.makeRandomNumber();
            String title = "Flint 회원 가입 인증 이메일 입니다."; // 이메일 제목
            String content =
                    "홈페이지를 방문해주셔서 감사합니다." +    //html 형식으로 작성
                            "<br><br>" +
                            "인증 번호는 " + authNumber + "입니다." +
                            "<br>" +
                            "해당 인증번호를 인증번호 확인란에 기입하여 주세요."; //이메일 내용 삽입
            sendMailService.sendEmail(email, title, content);
            redisUtil.saveAuthNumber(key, String.valueOf(authNumber), EXPIRATION);  //인증번호 검증을 위해 레디스에 저장
            return new EmailAuthNumberRespose(authNumber);
        }
        throw new FlintCustomException(HttpStatus.TOO_MANY_REQUESTS, ResultCode.USER_MANY_REQUEST);
    }
    @Transactional
    public AuthenticationResponse successEmailAuth(SuccessUniversityAuthRequest request, Long id) {
        if (!(String.valueOf(redisUtil.findEmailAuthNumberByKey(id)).equals(String.valueOf(request.getAuthNumber()))))  //레디스에 저장한 인증번호와 다르다면 에러코드 보냄
            throw new FlintCustomException(HttpStatus.BAD_REQUEST, ResultCode.MAIL_AUTHNUMBER_NOT);
        Member member = memberService.getMember(id);
        member.updateAuthority(Authority.AUTHUSER); //인증 한 유저로 권한 변경
        idCardService.saveFrontIdCard(member, request); //명함 자동 생성
        return authenticationService.generateToken(member); //인증 한 유저권한을 담은 토큰 발급
    }


}
