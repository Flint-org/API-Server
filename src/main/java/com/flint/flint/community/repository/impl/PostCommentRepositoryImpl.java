package com.flint.flint.community.repository.impl;

import com.flint.flint.community.domain.post.PostComment;
import com.flint.flint.community.domain.post.QPostComment;
import com.flint.flint.community.repository.custom.PostCommentRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.flint.flint.community.domain.post.QPostComment.postComment;

/**
 * @author 정순원
 * @since 2023-10-10
 */
@Repository
@RequiredArgsConstructor
public class PostCommentRepositoryImpl implements PostCommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostComment> findByPostId(Long postId) {
        return queryFactory
                .selectFrom(postComment)
                .join(postComment.parentComment).fetchJoin()
                .where(postComment.post.id.eq(postId))
                .orderBy(postComment.parentComment.id.asc().nullsFirst(),
                        postComment.createdAt.asc())
                .fetch();
    }

}
