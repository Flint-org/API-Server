package com.flint.flint.community.service;

import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.common.spec.ResultCode;
import com.flint.flint.community.domain.post.Post;
import com.flint.flint.community.domain.post.PostReport;
import com.flint.flint.community.repository.PostReportRepository;
import com.flint.flint.community.repository.post.PostRepository;
import com.flint.flint.mail.service.ReportEmailService;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostReportService {

    private final ReportEmailService reportEmailService;
    private final PostReportRepository postReportRepository;
    private final MemberService memberService;
    private final PostRepository postRepository;

    @Transactional
    public void reportPost(String providerId, long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, ResultCode.POST_NOT_FOUND));
        Member member = memberService.getMemberByProviderId(providerId);
        hasReportedPost(member, post); //신고한 이력이 있는지 검사
        reportEmailService.sendToAdmin(post.getTitle(), post.getContents());
        PostReport postReport = PostReport.builder()
                .post(post)
                .member(member)
                .build();
        postReportRepository.save(postReport);
    }

    private void hasReportedPost(Member member, Post post) {
        if (postReportRepository.existsByMemberAndPost(member, post))
            throw new FlintCustomException(HttpStatus.TOO_MANY_REQUESTS, ResultCode.USER_ALREADY_REPORTED);
    }

}
