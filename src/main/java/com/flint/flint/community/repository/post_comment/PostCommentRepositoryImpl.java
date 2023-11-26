package com.flint.flint.community.repository.impl;

import com.flint.flint.community.dto.response.PostCommentResponse;
import com.flint.flint.community.dto.response.QPostCommentResponse;
import com.flint.flint.community.repository.custom.PostCommentRepositoryCustom;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.flint.flint.asset.domain.QUniversityAsset.universityAsset;
import static com.flint.flint.community.domain.post.QPostComment.postComment;
import static com.flint.flint.community.domain.post.QPostCommentLike.postCommentLike;
import static com.flint.flint.idcard.domain.QIdCard.idCard;

/**
 * @author 정순원
 * @since 2023-10-10
 */
@Repository
@RequiredArgsConstructor
public class PostCommentRepositoryImpl implements PostCommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostCommentResponse> findByPostId(Long postId) {
        JPQLQuery<Long> likeCountSubQuery = JPAExpressions
                .select(postCommentLike.count())
                .from(postCommentLike)
                .where(postCommentLike.postComment.eq(postComment));

        JPQLQuery<String> logoUrlSubQuery = JPAExpressions
                .select(universityAsset.logoUrl)
                .from(idCard)
                .innerJoin(universityAsset).on(universityAsset.universityName.eq(idCard.university))
                .where(idCard.member.id.eq(postComment.member.id));

        return queryFactory
                .select(new QPostCommentResponse(
                        postComment.parentComment.id,
                        postComment.id,
                        postComment.contents,
                        postComment.createdAt,
                        postComment.member.id,
                        likeCountSubQuery,
                        logoUrlSubQuery))
                .from(postComment)
                .where(postComment.post.id.eq(postId))
                .orderBy(postComment.createdAt.asc())
                .fetch();
    }
}