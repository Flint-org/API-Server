package com.flint.flint.community.repository;

import com.flint.flint.community.domain.board.Board;
import com.flint.flint.community.domain.board.BoardBookmark;
import com.flint.flint.member.domain.main.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardBookmarkRepository extends JpaRepository<BoardBookmark, Long> {
    Optional<BoardBookmark> findBoardBookmarkByMemberAndBoard(Member member, Board board);

    @Query("SELECT bm FROM BoardBookmark bm WHERE bm.member = :member ORDER BY bm.board.boardType")
    List<BoardBookmark> findBoardBookmarksByMember(Member member);
}
