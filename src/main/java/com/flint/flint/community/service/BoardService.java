package com.flint.flint.community.service;

import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.community.domain.board.Board;
import com.flint.flint.community.domain.board.MajorBoard;
import com.flint.flint.community.dto.response.*;
import com.flint.flint.community.repository.BoardRepository;
import com.flint.flint.community.repository.MajorBoardRepository;
import com.flint.flint.community.spec.BoardType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.flint.flint.common.spec.ResultCode.*;

/**
 * 게시판 서비스
 *
 * @author 신승건
 * @since 2023-08-21
 */
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MajorBoardRepository majorBoardRepository;

    /**
     * 일반 게시판 목록 조회
     *
     * @return 일반 게시판 정보가 담긴 목록
     */
    @Transactional(readOnly = true)
    public List<GeneralBoardResponse> getGeneralBoardList() {
        List<Board> boards = boardRepository.findBoardsByBoardType(BoardType.GENERAL);

        return boards.stream().map(b -> GeneralBoardResponse.builder()
                        .boardId(b.getId())
                        .boardName(b.getGeneralBoardName())
                        .build())
                .toList();
    }

    /**
     * 대분류 & 소분류 계층구조로 모든 전공 게시판 목록을 조회
     *
     * @return 계층구조화된 전공 게시판 목록
     */
    @Transactional(readOnly = true)
    public List<MajorBoardResponse> getHierarchyMajorBoardList() {
        // 대분류 전공 게시판 필드(부모)가 null 인 전공 게시판 조회: 대분류만 조회
        List<MajorBoard> majorBoards = majorBoardRepository.findMajorBoardsByUpperMajorBoardIsNull();

        return majorBoards.stream().map(major -> MajorBoardResponse.builder()
                .boardId(major.getBoard().getId()) // 최상위 게시판 ID
                .upperMajor(UpperMajorInfoResponse.builder() // 대분류 전공 게시판
                        .upperMajorId(major.getId())
                        .upperMajorName(major.getName())
                        .lowerMajors(major.getLowerMajorBoards().stream().map( // parent로부터 소분류 전공 게시판 가져오기
                                        lower -> LowerMajorBoardResponse.builder()
                                                .lowerMajorId(lower.getId())
                                                .lowerMajorName(lower.getName())
                                                .build()
                                ).toList()
                        ).build()
                ).build()
        ).toList();
    }

    /**
     * 대분류 전공 게시판 목록만 별도로 조회
     *
     * @return 최상위 게시판 ID와 대분류 전공 게시판 정보를 담은 리스트
     */
    @Transactional(readOnly = true)
    public List<UpperMajorListResponse> getOnlyUpperMajorList() {
        List<MajorBoard> majorBoards = majorBoardRepository.findMajorBoardsByUpperMajorBoardIsNull();

        return majorBoards.stream().map(
                majorBoard -> UpperMajorListResponse.builder()
                        .boardId(majorBoard.getBoard().getId())
                        .upperMajorId(majorBoard.getId())
                        .upperMajorName(majorBoard.getName())
                        .build()
        ).toList();
    }

    /**
     * 특정 대분류 전공 게시판에 해당하는 소분류 전공 게시판 목록을 조회
     *
     * @param upperMajorId 대분류 전공 게시판 ID
     * @return 특정 대분류 전공 게시판 정보와 소분류 전공 게시판 목록
     */
    @Transactional(readOnly = true)
    public UpperMajorInfoResponse getLowerMajorListByUpperMajor(Long upperMajorId) {
        // 해당 전공 게시판이 존재하는지 검증
        MajorBoard upperMajor = majorBoardRepository.findById(upperMajorId)
                .orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, MAJOR_BOARD_NOT_FOUND));

        List<MajorBoard> lowerMajors = majorBoardRepository.findMajorBoardsByUpperMajorBoard(upperMajor);

        return UpperMajorInfoResponse.builder()
                .upperMajorId(upperMajor.getId())
                .upperMajorName(upperMajor.getName())
                .lowerMajors(lowerMajors.stream().map(lower -> LowerMajorBoardResponse.builder()
                                .lowerMajorId(lower.getId())
                                .lowerMajorName(lower.getName())
                                .build())
                        .toList())
                .build();
    }

    public Board getBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, BOARD_NOT_FOUND));
    }
}
