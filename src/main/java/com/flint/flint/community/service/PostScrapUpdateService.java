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

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostScrapUpdateService {

    private final PostScrapRepository postScrapRepository;
    private final MemberService memberService;
    private final PostRepository postRepository;

    public void touchPostScrap(String providerId, long postId) {
        Optional<PostScrap> optinalScrap = postScrapRepository.findByProviderIdAndPostId(providerId, postId);
        if (optinalScrap.isPresent())
            postScrapRepository.delete(optinalScrap.get());
        createScrap(providerId, postId);
    }

    private void createScrap(String providerId, long postId) {
        Member member = memberService.getMemberByProviderId(providerId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, ResultCode.POST_NOT_FOUND));
        PostScrap postScrap = PostScrap.builder()
                .member(member)
                .post(post)
                .build();
        postScrapRepository.save(postScrap);
    }
}
