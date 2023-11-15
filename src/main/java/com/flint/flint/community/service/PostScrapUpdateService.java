package com.flint.flint.community.service;

import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.common.spec.ResultCode;
import com.flint.flint.community.domain.post.Post;
import com.flint.flint.community.domain.post.PostScrap;
import com.flint.flint.community.repository.PostRepository;
import com.flint.flint.community.repository.PostScrapRepository;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostScrapUpdateService {

    private final PostScrapRepository postScrapRepository;
    private final MemberService memberService;
    private final PostRepository postRepository;

    @Transactional
    public void createScrap(String providerId, long postId) {
        Member member = memberService.getMemberByProviderId(providerId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, ResultCode.POST_NOT_FOUND));
        PostScrap postScrap = PostScrap.builder()
                .member(member)
                .post(post)
                .build();
        postScrapRepository.save(postScrap);
    }

    @Transactional
    public void deleteScrap(String providerId, long postId) {
        PostScrap postScrap = postScrapRepository.findByProviderIdAndPostId(providerId, postId).orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, ResultCode.POST_SCRAP_NOT_FOUND));
        postScrapRepository.delete(postScrap);
    }
}
