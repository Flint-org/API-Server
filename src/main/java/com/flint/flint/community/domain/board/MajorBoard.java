package com.flint.flint.community.domain.board;

import com.flint.flint.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 전공 게시판 엔티티 (self-referencing)
 *
 * @author 신승건
 * @since 2023-08-21
 */
@Entity
@Getter
@NoArgsConstructor
public class MajorBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MAJOR_BOARD_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board; // child도 가지도록 변경 (2023-10-13)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UPPER_BOARD_ID")
    private MajorBoard upperMajorBoard; // parent

    @OneToMany(mappedBy = "upperMajorBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MajorBoard> lowerMajorBoards = new ArrayList<>(); // children

    @Column(nullable = false)
    private String name;

    @Builder
    public MajorBoard(Board board, String name, MajorBoard upperMajorBoard, List<MajorBoard> lowerMajorBoards) {
        this.board = board;
        this.name = name;
        this.upperMajorBoard = upperMajorBoard;
        this.lowerMajorBoards = lowerMajorBoards;
    }

    public void updateLowerMajorBoards(List<MajorBoard> lowerMajorBoards){
        this.lowerMajorBoards = lowerMajorBoards;
    }
}
