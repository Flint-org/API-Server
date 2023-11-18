package com.flint.flint.mail.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author 정순원
 * @Since 2023-11-08
 */
@RequiredArgsConstructor
@Service
public class ReportEmailService {

    private static final String ADMIN_EMAIL = "ljh99041248@gmail.com";

    private final SendMailService sendMailService;

    @Transactional
    public void sendToAdmin(String reportedTitle, String reportedContents) {
        String title = "Flint 게시글 신고 내용입니다."; // 이메일 제목
        String emailContents =
                "신고된 게시글 내용입니다." +    //html 형식으로 작성
                        "<br><br>" +
                        "제목:" + reportedContents +
                        "<br>" +
                        "내용: " +
                        "<br>" +
                        reportedContents; //이메일 내용 삽입
        sendMailService.sendEmail(ADMIN_EMAIL, title, emailContents);
    }
}
