package com.flint.flint.community.repository;

import com.flint.flint.community.domain.post.Post;
import com.flint.flint.community.dto.response.PostListResponse;
import com.flint.flint.community.spec.SortStrategy;

import java.util.List;

public interface PostRepositoryCustom {
    List<PostListResponse> findPostsWithBoardByPaging(Long boardId, Post cursorPost, Long size, SortStrategy sort);
}
