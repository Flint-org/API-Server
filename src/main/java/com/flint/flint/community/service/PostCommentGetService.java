package com.flint.flint.community.service;

import com.flint.flint.community.dto.response.PostCommentResponse;
import com.flint.flint.community.repository.post_comment.PostCommentRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author 정순원
 * @since 2023-10-09
 */
@Service
@RequiredArgsConstructor
public class PostCommentGetService {

    private final static int PRIORITY_LIKE_CRITERIA = 3;
    private final PostCommentRepositoryCustom postCommentRepositoryCustom;

    @Transactional(readOnly = true)
    public List<PostCommentResponse> getPostComments(long postId) {
        List<PostCommentResponse> commentResponses = postCommentRepositoryCustom.findByPostId(postId);

        // 공감수에 따라 댓글 분류
        List<PostCommentResponse> highLikesParentComments = new ArrayList<>();
        List<PostCommentResponse> lowLikesParentComments = new ArrayList<>();
        List<PostCommentResponse> highLikesChildComments = new ArrayList<>();
        List<PostCommentResponse> lowLikesChildComments = new ArrayList<>();

        for (PostCommentResponse comment : commentResponses) {
            if(comment.getParentCommentId() == null) {
                if (comment.getLikeCount() != null && comment.getLikeCount() >= PRIORITY_LIKE_CRITERIA) {
                    highLikesParentComments.add(comment);
                } else {
                    lowLikesParentComments.add(comment);
                }
            } else {
                if (comment.getLikeCount() != null && comment.getLikeCount() >= PRIORITY_LIKE_CRITERIA) {
                    highLikesChildComments.add(comment);
                } else {
                    lowLikesChildComments.add(comment);
                }
            }
        }

        // 공감수가 높은 댓글을 공감수 내림차순으로 정렬
        highLikesParentComments.sort(Comparator.comparing(PostCommentResponse::getLikeCount).reversed());
        highLikesChildComments.sort(Comparator.comparing(PostCommentResponse::getLikeCount).reversed());

        // 공감수가 낮은 댓글을 생성 시간 오름차순으로 정렬
        lowLikesParentComments.sort(Comparator.comparing(PostCommentResponse::getCreatedAt));
        lowLikesChildComments.sort(Comparator.comparing(PostCommentResponse::getCreatedAt));



        // 최종 결과 리스트 생성
        List<PostCommentResponse> sortedComments = new ArrayList<>();
        sortedComments.addAll(highLikesParentComments);
        sortedComments.addAll(lowLikesParentComments);
        sortedComments.addAll(highLikesChildComments);
        sortedComments.addAll(lowLikesChildComments);

        // 대댓글 구조 재구성
        Map<Long, PostCommentResponse> commentMap = new HashMap<>();
        List<PostCommentResponse> parentCommentResponses = new ArrayList<>();

        for (PostCommentResponse commentResponse : sortedComments) {
            commentMap.put(commentResponse.getPostCommentId(), commentResponse);
            if (commentResponse.getParentCommentId() == null) {
                parentCommentResponses.add(commentResponse);
            } else {
                if (commentMap.containsKey(commentResponse.getParentCommentId())) {
                    commentMap.get(commentResponse.getParentCommentId()).getReplies().add(commentResponse);
                }
            }
        }

        return parentCommentResponses;
    }
}
