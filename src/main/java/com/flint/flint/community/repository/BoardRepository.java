package com.flint.flint.community.repository;

import com.flint.flint.community.domain.board.Board;
import com.flint.flint.community.spec.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findBoardsByBoardType(BoardType type);

    Optional<Board> findBoardByGeneralBoardName(String name);

    @Query("SELECT mb.board FROM MajorBoard mb WHERE mb.name = :name")
    Optional<Board> findBoardByMajorName(@Param("name") String name);
}
