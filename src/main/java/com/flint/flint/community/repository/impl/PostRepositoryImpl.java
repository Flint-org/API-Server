package com.flint.flint.community.repository.impl;

import com.flint.flint.community.domain.post.Post;
import com.flint.flint.community.repository.custom.PostRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.flint.flint.community.domain.post.QPost.post;

/**
 * @author 정순원
 * @since 2023-11-06
 */
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 전체게시판에서
     * 제목이나 내용에 키워드가 포함된 글 최신순으로 가져오기
     */
    @Override
    public Page<Post> findByTitleOrContentsContaining(String keyword, Pageable pageable) {
        List<Post> posts = queryFactory.selectFrom(post)
                .where(containsTitleOrContent(keyword))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(posts, pageable, posts.size());
    }


    /**
     * 특정 게시판에서
     * 제목이나 내용에 키워드가 포함된 글 최신순으로 가져오기
     */
    @Override
    public Page<Post> findByTitleOrContentsContainingAndBoard(long boardId, String keyword, Pageable pageable) {

        List<Post> posts = queryFactory.selectFrom(post)
                .where(containsTitleOrContent(keyword).and(isBoard(boardId)))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(posts, pageable, posts.size());
    }

    private BooleanExpression containsTitleOrContent(String keyword) {
        return post.title.contains(keyword).or(post.contents.contains(keyword));
    }

    private BooleanExpression isBoard(Long boardId) {
        return post.board.id.eq(boardId);
    }}
