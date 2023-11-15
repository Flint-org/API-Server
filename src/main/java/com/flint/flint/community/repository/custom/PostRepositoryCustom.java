package com.flint.flint.community.repository.custom;

import com.flint.flint.community.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * @author 정순원
 * @since 2023-11-06
 */
public interface PostRepositoryCustom {

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
