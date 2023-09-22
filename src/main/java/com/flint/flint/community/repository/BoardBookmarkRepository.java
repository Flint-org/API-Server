package com.flint.flint.community.repository;

import com.flint.flint.community.domain.board.Board;
import com.flint.flint.community.domain.board.BoardBookmark;
import com.flint.flint.member.domain.main.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardBookmarkRepository extends JpaRepository<BoardBookmark, Long> {
    boolean existsByMemberAndBoard(Member member, Board board);
}
