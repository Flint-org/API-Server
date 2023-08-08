package com.flint.flint.community.domain;

import com.flint.flint.common.BaseTimeEntity;
import com.flint.flint.community.specification.BoardCategoryType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class BoardCategory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_CATEGORY_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    private BoardCategoryType boardCategoryType;

    @Column(length = 20, nullable = false)
    private String upperName;

    @Column(length = 20)
    private String lowerName;

    @Builder
    public BoardCategory(BoardCategoryType boardCategoryType, String upperName, String lowerName) {
        this.boardCategoryType = boardCategoryType;
        this.upperName = upperName;
        this.lowerName = lowerName;
    }
}
