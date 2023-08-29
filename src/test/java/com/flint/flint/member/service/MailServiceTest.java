package com.flint.flint.member.service;

import com.flint.flint.config.MailConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @Author 정순원
 * @Since 2023-08-29
 */
@SpringBootTest(classes = {MailConfig.class, MailService.class, JavaMailSender.class})
class MailServiceTest {

    @Autowired
    MailService mailService;

    @Autowired
    private JavaMailSender mailSender;

    String to = "jsw5913@naver.com";

    /**
     * 인터넷 연결되어 있어야 합니다
     */
    @Test
    @DisplayName("테스트 이메일 발송입니다")
    public void MailsendTest() {
        mailService.sendCodeEmail(to);
    }
}