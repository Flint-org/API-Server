package com.flint.flint.community.repository;

import com.flint.flint.community.domain.board.MajorBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MajorBoardRepository extends JpaRepository<MajorBoard, Long> {

    List<MajorBoard> findMajorBoardsByUpperMajorBoardIsNull();

    List<MajorBoard> findMajorBoardsByUpperMajorBoard(MajorBoard upperMajor);

    Optional<MajorBoard> findMajorBoardByName(String majorName);

    Optional<MajorBoard> findMajorBoardByUpperMajorBoardIsNullAndId(Long upperMajorId);
}
