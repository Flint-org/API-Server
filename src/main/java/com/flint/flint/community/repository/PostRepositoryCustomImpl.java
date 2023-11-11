package com.flint.flint.community.repository;

import com.flint.flint.community.domain.post.Post;
import com.flint.flint.community.dto.response.PostListResponse;
import com.flint.flint.community.spec.SortStrategy;
import com.querydsl.core.types.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.flint.flint.community.domain.post.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostListResponse> findPostsWithBoardByPaging(Long boardId, Post cursorPost, Long size, SortStrategy sort) {
        return queryFactory
                .selectFrom(post)
                .where(
                        filterBoard(boardId),
                        filterPaging(cursorPost, sort),
                        filterSameCursor(cursorPost, sort)
                )
                .limit(size)
                .orderBy(sortStrategy(sort), post.id.desc()) // 좋아요 수가 같으면 최신 순
                .fetch()
                .stream()
                .map(p -> PostListResponse.builder()
                        .thumbnailImage(p.getPostImages().isEmpty() ? null : p.getPostImages().get(0).getImgUrl())
                        .postId(p.getId())
                        .title(p.getTitle())
                        .scrapCount(p.getPostScraps().size())
                        .commentCount(p.getPostComments().size())
                        .likeCount(p.getPostLikes().size())
                        .createdAt(p.getCreatedAt())
                        .build())
                .toList();
    }

    private Predicate filterBoard(Long boardId) {
        if (boardId == null) {
            return null;
        }
        return post.board.id.eq(boardId);
    }

    private Predicate filterPaging(Post cursorPost, SortStrategy strategy) {
        if (cursorPost == null) {
            return null;
        }

        if (strategy == SortStrategy.POPULAR) {
            return post.postLikes.size().loe(cursorPost.getPostLikes().size());
        }
        // ID가 큰 값이 최신이기에 그 다음 최신 글은 ID가 less than한 게시글
        return post.id.lt(cursorPost.getId());
    }

    private OrderSpecifier<?> sortStrategy(SortStrategy strategy) {
        if (strategy == null) {
            return new OrderSpecifier<>(Order.DESC, post.id);
        }

        switch (strategy) {
            case CHRONOLOGICAL -> {
                return new OrderSpecifier<>(Order.DESC, post.id);
            }
            case POPULAR -> {
                return new OrderSpecifier<>(Order.DESC, post.postLikes.size());
            }
        }
        return null;
    }

    private Predicate filterSameCursor(Post cursorPost, SortStrategy sortStrategy) {
        if (cursorPost == null || sortStrategy == null) {
            return null;
        }

        if (sortStrategy == SortStrategy.POPULAR) {
            // 인기순인 경우 현재 커서의 좋아요 개수보다는 같지 않거나 id가 작아야함
            // 즉, 더 적은 좋아요 수를 가지거나, 더 작은 ID를 가진 게시글이 해당되는 조건
            return ExpressionUtils.or(post.postLikes.size().ne(cursorPost.getPostLikes().size())
                    , post.id.lt(cursorPost.getId()));
        }
        return null;
    }
}
