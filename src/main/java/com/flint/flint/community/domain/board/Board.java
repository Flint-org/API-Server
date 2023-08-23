package com.flint.flint.community.domain.board;

import com.flint.flint.common.BaseTimeEntity;
import com.flint.flint.community.spec.BoardType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    private String generalBoardName;

    @Builder
    public Board(BoardType boardType, String generalBoardName) {
        this.boardType = boardType;
        this.generalBoardName = generalBoardName;
    }
}
