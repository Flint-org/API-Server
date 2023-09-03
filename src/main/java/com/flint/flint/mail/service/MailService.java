package com.flint.flint.mail.service;

import com.flint.flint.common.ResponseForm;
import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.common.spec.ResultCode;
import com.flint.flint.mail.dto.request.VeriyEmailAuthnumberRequest;
import com.flint.flint.mail.dto.response.EmailAuthNumberRespose;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.domain.spec.Authority;
import com.flint.flint.member.service.MemberService;
import com.flint.flint.redis.RedisUtil;
import com.flint.flint.security.auth.AuthenticationService;
import com.flint.flint.security.auth.dto.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.flint.flint.common.spec.ResultCode.MAIL_AUTHNUMBER_NOT;

/**
 * @Author 정순원
 * @Since 2023-08-31
 */
@Service
@RequiredArgsConstructor
public class MailService {

    private final static Long Expiration = 1800000L; //한시간

    @Value("${email.id}")
    private String fromId;
    private int authNumber;

    private final JavaMailSender mailSender;
    private final RateLimitService rateLimitService;
    private final MemberService memberService;
    private final AuthenticationService authenticationService;
    private final RedisUtil redisUtil;

    public EmailAuthNumberRespose sendCodeEmail(String email, Long key) {
        if (rateLimitService.checkAPICall(key)) { //API호출 횟수 검사
            makeRandomNumber();
            String title = "Flint 회원 가입 인증 이메일 입니다."; // 이메일 제목
            String content =
                    "홈페이지를 방문해주셔서 감사합니다." +    //html 형식으로 작성
                            "<br><br>" +
                            "인증 번호는 " + authNumber + "입니다." +
                            "<br>" +
                            "해당 인증번호를 인증번호 확인란에 기입하여 주세요."; //이메일 내용 삽입
            sendEmail(email, title, content);
            redisUtil.saveAuthNumber(key, String.valueOf(authNumber), Expiration);  //인증번호 검증을 위해 레디스에 저장
            return new EmailAuthNumberRespose(authNumber);
        }
        throw new FlintCustomException(HttpStatus.TOO_MANY_REQUESTS, ResultCode.USER_MANY_REQUEST);
    }

    private void makeRandomNumber() {
        // 난수의 범위 111111 ~ 999999 (6자리 난수)
        Random r = new Random();
        int checkNum = r.nextInt(888888) + 111111;
        authNumber = checkNum;
    }

    private void sendEmail(String to, String subject, String content) {
        MimeMessagePreparator messagePreparator =
                mimeMessage -> {
                    //true는 멀티파트 메세지를 사용하겠다는 의미
                    final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                    helper.setFrom(fromId);
                    helper.setTo(to);
                    helper.setSubject(subject);
                    helper.setText(content, true);
                };
        mailSender.send(messagePreparator);
    }

    public AuthenticationResponse successEmailAuth(VeriyEmailAuthnumberRequest request, Long key) {
        if (!(String.valueOf(redisUtil.findEmailAuthNumberByKey(key)).equals(String.valueOf(request.getAuthNumber()))))  //레디스에 저장한 인증번호와 다르다면 에러코드 보냄
            throw new FlintCustomException(HttpStatus.BAD_REQUEST, ResultCode.MAIL_AUTHNUMBER_NOT);
        Member member = memberService.getMember(key);
        member.updateAuthority(Authority.AUTHUSER);
        return authenticationService.generateToken(member); //인증 한 유저권한을 담은 토큰 발급
    }

}