package com.flint.flint.community.repository;

import com.flint.flint.community.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * 전체게시판에서
     * 제목이나 내용에 키워드가 포함된 글 최신순으로 가져오기
     */
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.contents LIKE %:keyword% ORDER BY p.createdAt DESC")
    List<Post> findByTitleContainingOrContentsContainingOrderByCreatedAtDesc(@Param("keyword") String keyword);

    /**
     * 특정 게시판에서
     * 제목이나 내용에 키워드가 포함된 글 최신순으로 가져오기
     */
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.contents LIKE %:keyword% And p.board.id = :boardId ORDER BY p.createdAt DESC")
    List<Post> findByTitleContainingOrContentsContainingAndBoardOrderByCreatedAtDesc(@Param("boardId") long boardId, @Param("keyword") String keyword);

}
