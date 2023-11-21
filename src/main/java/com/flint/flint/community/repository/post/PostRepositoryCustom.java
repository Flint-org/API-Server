package com.flint.flint.community.repository.post;

import com.flint.flint.community.domain.post.Post;
import com.flint.flint.community.dto.response.PostListResponse;
import com.flint.flint.community.spec.SortStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {
    List<PostListResponse> findPostsWithBoardByPaging(Long boardId, Post cursorPost, Long size, SortStrategy sort);

    /**
     * 전체게시판에서
     * 제목이나 내용에 키워드가 포함된 글 최신순으로 가져오기
     */
    Page<Post> findByTitleOrContentsContaining(String keyword, Pageable pageable);

    /**
     * 특정 게시판에서
     * 제목이나 내용에 키워드가 포함된 글 최신순으로 가져오기
     */
    Page<Post> findByTitleOrContentsContainingAndBoard(long boardId, String keyword, Pageable pageable);


}
