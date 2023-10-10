package com.flint.flint.community.service;

import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.common.spec.ResultCode;
import com.flint.flint.community.domain.post.Post;
import com.flint.flint.community.domain.post.PostComment;
import com.flint.flint.community.dto.request.PostCommentUpdateRequest;
import com.flint.flint.community.dto.response.PostCommentUpdateResponse;
import com.flint.flint.community.repository.PostCommentRepository;
import com.flint.flint.community.repository.PostRepository;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author 정순원
 * @since 2023-10-04
 */
@Service
@RequiredArgsConstructor
public class PostCommentUpdateService {

    private final PostCommentRepository postCommentRepository;
    private final MemberService memberService;
    private final PostRepository postRepository;

    @Transactional
    public PostCommentUpdateResponse createPostComment(String providerId, long postId, PostCommentUpdateRequest createDTO) {
        PostComment postComment = buildPostComment(providerId, postId, createDTO);

        if (createDTO.getParentCommentId() == null) { // 댓글일시
            postComment.setParentComment(null);
        } else {                                    //대댓글일시
            PostComment parentComment = postCommentRepository.findById(createDTO.getParentCommentId()).orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, ResultCode.POST_COMMENT_NOT_FOUND));
            parentComment.addCommentReply(postComment);
        }
        postCommentRepository.save(postComment);
        return new PostCommentUpdateResponse(postComment.getId());
    }

    @Transactional
    public void deletePostComment(String providerId, long postCommentId) {
        isWriter(providerId, postCommentId);  //글쓴이가 맞는지
        postCommentRepository.deleteById(postCommentId);
    }

    @Transactional
    public void updatePostComment(String providerId, long postCommentId, PostCommentUpdateRequest updateRequest) {
        isWriter(providerId, postCommentId);
        PostComment postComment = postCommentRepository.findById(postCommentId).orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, ResultCode.POST_COMMENT_NOT_FOUND));
        postComment.updateContent(updateRequest.getContents());
    }

    private PostComment buildPostComment(String providerId, long postId, PostCommentUpdateRequest createDTO) {
        Member member = memberService.getMemberByProviderId(providerId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, ResultCode.POST_NOT_FOUND));

        return PostComment.builder()
                .member(member)
                .post(post)
                .contents(createDTO.getContents())
                .build();
    }

    private void isWriter(String providerId, long postCommentId) {
        PostComment postComment = postCommentRepository.findById(postCommentId).orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, ResultCode.POST_COMMENT_NOT_FOUND));
        if (providerId != postComment.getMember().getProviderId()) {
            throw new FlintCustomException(HttpStatus.FORBIDDEN, ResultCode.POST_COMMENT_NOT_WRITER);
        }
    }
}
