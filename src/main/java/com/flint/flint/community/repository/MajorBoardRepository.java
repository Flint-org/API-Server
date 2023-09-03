package com.flint.flint.community.repository;

import com.flint.flint.community.domain.board.MajorBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MajorBoardRepository extends JpaRepository<MajorBoard, Long> {

    List<MajorBoard> findMajorBoardsByUpperMajorBoardIsNull();

    List<MajorBoard> findMajorBoardsByUpperMajorBoard(MajorBoard upperMajor);
}
