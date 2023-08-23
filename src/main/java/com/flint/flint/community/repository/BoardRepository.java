package com.flint.flint.community.repository;

import com.flint.flint.community.domain.board.Board;
import com.flint.flint.community.spec.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findBoardsByBoardType(BoardType type);
}
