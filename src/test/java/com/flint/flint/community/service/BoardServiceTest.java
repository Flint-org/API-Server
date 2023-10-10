package com.flint.flint.community.service;

import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.common.spec.ResultCode;
import com.flint.flint.community.domain.board.Board;
import com.flint.flint.community.domain.board.MajorBoard;
import com.flint.flint.community.dto.response.GeneralBoardResponse;
import com.flint.flint.community.dto.response.MajorBoardResponse;
import com.flint.flint.community.dto.response.UpperMajorInfoResponse;
import com.flint.flint.community.dto.response.UpperMajorListResponse;
import com.flint.flint.community.repository.BoardBookmarkRepository;
import com.flint.flint.community.repository.BoardRepository;
import com.flint.flint.community.repository.MajorBoardRepository;
import com.flint.flint.community.spec.BoardType;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.repository.MemberRepository;
import com.flint.flint.member.spec.Authority;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardServiceTest {

    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardBookmarkRepository bookmarkRepository;
    @Autowired
    private MajorBoardRepository majorBoardRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void initBoard() {
        Board board = Board.builder()
                .boardType(BoardType.GENERAL)
                .generalBoardName("자유게시판")
                .build();

        Board board2 = Board.builder()
                .boardType(BoardType.GENERAL)
                .generalBoardName("새내기게시판")
                .build();

        boardRepository.save(board);
        boardRepository.save(board2);

        Board majorBoard1 = Board.builder().boardType(BoardType.MAJOR)
                .generalBoardName(null)
                .build();

        Board majorBoard2 = Board.builder().boardType(BoardType.MAJOR)
                .generalBoardName(null)
                .build();

        Board majorBoard3 = Board.builder().boardType(BoardType.MAJOR)
                .generalBoardName(null)
                .build();

        boardRepository.save(majorBoard1);
        boardRepository.save(majorBoard2);
        boardRepository.save(majorBoard3);

        MajorBoard major1 = MajorBoard.builder()
                .board(majorBoard1)
                .name("공학계열")
                .upperMajorBoard(null)
                .build();

        MajorBoard major2 = MajorBoard.builder()
                .board(majorBoard2)
                .name("사회계열")
                .upperMajorBoard(null)
                .build();

        MajorBoard major3 = MajorBoard.builder()
                .board(majorBoard3)
                .name("교육계열")
                .upperMajorBoard(null)
                .build();

        MajorBoard savedMajor1 = majorBoardRepository.save(major1);
        MajorBoard savedMajor2 = majorBoardRepository.save(major2);
        MajorBoard savedMajor3 = majorBoardRepository.save(major3);

        MajorBoard lower11 = MajorBoard.builder()
                .board(null)
                .name("건축")
                .upperMajorBoard(savedMajor1)
                .lowerMajorBoards(null)
                .build();
        MajorBoard lower12 = MajorBoard.builder()
                .board(null)
                .name("기계 금속")
                .upperMajorBoard(savedMajor1)
                .lowerMajorBoards(null)
                .build();

        MajorBoard lower21 = MajorBoard.builder()
                .board(null)
                .name("경영 경제")
                .upperMajorBoard(savedMajor2)
                .lowerMajorBoards(null)
                .build();

        MajorBoard lower22 = MajorBoard.builder()
                .board(null)
                .name("법률")
                .upperMajorBoard(savedMajor2)
                .lowerMajorBoards(null)
                .build();

        MajorBoard lower31 = MajorBoard.builder()
                .board(null)
                .name("교육일반")
                .upperMajorBoard(savedMajor3)
                .lowerMajorBoards(null)
                .build();

        List<MajorBoard> major1s = new ArrayList<>();
        major1s.add(lower11);
        major1s.add(lower12);

        List<MajorBoard> major2s = new ArrayList<>();
        major2s.add(lower21);
        major2s.add(lower22);

        List<MajorBoard> major3s = new ArrayList<>();
        major3s.add(lower31);

        major1.updateLowerMajorBoards(major1s);
        major2.updateLowerMajorBoards(major2s);
        major3.updateLowerMajorBoards(major3s);
    }

    @DisplayName("일반 게시판 목록 조회에 성공한다.")
    @Test
    void getGeneralBoardList() {
        // given, when
        List<GeneralBoardResponse> generalBoards = boardService.getGeneralBoardList();

        // then
        assertEquals(2, generalBoards.size());
        assertEquals("자유게시판", generalBoards.get(0).getBoardName());
        assertEquals("새내기게시판", generalBoards.get(1).getBoardName());
    }

    @DisplayName("전체 전공 게시판 목록을 계층 구조로 조회에 성공한다.")
    @Test
    void getHierarchyMajorBoardList() {
        // given, when
        List<MajorBoardResponse> majorBoards = boardService.getHierarchyMajorBoardList();

        // then
        assertEquals(3, majorBoards.size());

        assertEquals("공학계열", majorBoards.get(0).getUpperMajor().getUpperMajorName());
        assertEquals(2, majorBoards.get(0).getUpperMajor().getLowerMajors().size());
        assertEquals("건축", majorBoards.get(0).getUpperMajor().getLowerMajors().get(0).getLowerMajorName());
        assertEquals("기계 금속", majorBoards.get(0).getUpperMajor().getLowerMajors().get(1).getLowerMajorName());

        assertEquals("사회계열", majorBoards.get(1).getUpperMajor().getUpperMajorName());
        assertEquals(2, majorBoards.get(1).getUpperMajor().getLowerMajors().size());
        assertEquals("경영 경제", majorBoards.get(1).getUpperMajor().getLowerMajors().get(0).getLowerMajorName());
        assertEquals("법률", majorBoards.get(1).getUpperMajor().getLowerMajors().get(1).getLowerMajorName());
    }

    @DisplayName("대분류 전공 게시판 목록 조회에 성공한다.")
    @Test
    void getOnlyUpperMajorList() {
        // given, when
        List<UpperMajorListResponse> upperMajors = boardService.getOnlyUpperMajorList();

        // then
        assertEquals(3, upperMajors.size());

        assertEquals("공학계열", upperMajors.get(0).getUpperMajorName());
        assertEquals("사회계열", upperMajors.get(1).getUpperMajorName());
        assertEquals("교육계열", upperMajors.get(2).getUpperMajorName());
    }

    @DisplayName("특정 대분류 전공 게시판으로 소분류 전공 게시판 목록 조회에 성공한다.")
    @Test
    void getLowerMajorListByUpperMajor() {
        // given
        List<MajorBoardResponse> majorBoards = boardService.getHierarchyMajorBoardList();

        Long upperMajorId1 = majorBoards.get(0).getUpperMajor().getUpperMajorId();
        Long upperMajorId2 = majorBoards.get(2).getUpperMajor().getUpperMajorId();

        // when
        UpperMajorInfoResponse lowerMajors1 = boardService.getLowerMajorListByUpperMajor(upperMajorId1);
        UpperMajorInfoResponse lowerMajors2 = boardService.getLowerMajorListByUpperMajor(upperMajorId2);

        // then
        assertEquals(2, lowerMajors1.getLowerMajors().size());
        assertEquals("공학계열", lowerMajors1.getUpperMajorName());
        assertEquals("건축", lowerMajors1.getLowerMajors().get(0).getLowerMajorName());
        assertEquals("기계 금속", lowerMajors1.getLowerMajors().get(1).getLowerMajorName());

        assertEquals(1, lowerMajors2.getLowerMajors().size());
        assertEquals("교육계열", lowerMajors2.getUpperMajorName());
        assertEquals("교육일반", lowerMajors2.getLowerMajors().get(0).getLowerMajorName());
    }

    @DisplayName("존재하지 않는 대분류 전공 게시판으로 소분류 게시판을 조회하면 예외가 발생한다.")
    @Test
    void checkNotExistUpperMajor() {
        // given
        Long upperMajorID = 1000000L;

        // when, then
        assertThatThrownBy(() -> boardService.getLowerMajorListByUpperMajor(upperMajorID))
                .isInstanceOf(FlintCustomException.class)
                .hasMessage(ResultCode.MAJOR_BOARD_NOT_FOUND.getMessage());
    }

    @DisplayName("유저가 게시판을 즐겨찾기에 등록하면, 성공적으로 처리된다.")
    @Test
    void bookmarkBoard() {
        // given
        Member member = Member.builder()
                .name("테스터")
                .providerId("kakao 12345")
                .email("test@test.com")
                .providerName("kakao")
                .authority(Authority.AUTHUSER)
                .build();

        Member save = memberRepository.save(member);

        // when
        Board board = boardRepository.findBoardByGeneralBoardName("자유게시판").get();
        MajorBoard majorBoard = majorBoardRepository.findMajorBoardByName("공학계열").get();

        boardService.bookmarkBoard(save.getProviderId(), board.getId());
        boardService.bookmarkBoard(save.getProviderId(), majorBoard.getBoard().getId());

        // then
        assertThat(bookmarkRepository.findBoardBookmarkByMemberAndBoard(save, board).isPresent()).isTrue();
        assertThat(bookmarkRepository.findBoardBookmarkByMemberAndBoard(save, majorBoard.getBoard()).isPresent()).isTrue();
    }

    @DisplayName("유저가 이미 즐겨찾기한 게시판을 즐겨찾기 등록에 시도할 시 예외가 발생한다.")
    @Test
    void bookmarkBoardWithAlreadyBookmarked() {
        // given
        Member member = Member.builder()
                .name("테스터")
                .providerId("kakao 12345")
                .email("test@test.com")
                .providerName("kakao")
                .authority(Authority.AUTHUSER)
                .build();

        Member save = memberRepository.save(member);

        // when
        Board board = boardRepository.findBoardByGeneralBoardName("자유게시판").get();

        boardService.bookmarkBoard(save.getProviderId(), board.getId());

        // then
        assertThatThrownBy(() -> boardService.bookmarkBoard(save.getProviderId(), board.getId()))
                .isInstanceOf(FlintCustomException.class)
                .hasMessage(ResultCode.ALREADY_BOOKMARKED_BOARD.getMessage());
    }

    @DisplayName("유저가 즐겨찾기되지 않은 게시판을 해제에 시도할 시 예외가 발생한다.")
    @Test
    void deleteBookmarkBoardWithNoBookmarked() {
        // given
        Member member = Member.builder()
                .name("테스터")
                .providerId("kakao 12345")
                .email("test@test.com")
                .providerName("kakao")
                .authority(Authority.AUTHUSER)
                .build();

        Member save = memberRepository.save(member);

        // when
        Board board = boardRepository.findBoardByGeneralBoardName("자유게시판").get();

        // then
        assertThatThrownBy(() -> boardService.deleteBookmarkBoard(save.getProviderId(), board.getId()))
                .isInstanceOf(FlintCustomException.class)
                .hasMessage(ResultCode.UNKNOWN_BOOKMARK_BOARD.getMessage());
    }

    @DisplayName("유저가 즐겨찾기된 게시판에 대해 즐겨찾기 해제 시, 해제에 성공한다.")
    @Test
    void deleteBookmarkBoard() {
        // given
        Member member = Member.builder()
                .name("테스터")
                .providerId("kakao 12345")
                .email("test@test.com")
                .providerName("kakao")
                .authority(Authority.AUTHUSER)
                .build();

        Member save = memberRepository.save(member);

        Board board = boardRepository.findBoardByGeneralBoardName("자유게시판").get();
        boardService.bookmarkBoard(save.getProviderId(), board.getId());

        // when
        boardService.deleteBookmarkBoard(save.getProviderId(), board.getId());

        // then
        assertThat(bookmarkRepository.findBoardBookmarkByMemberAndBoard(member, board).isEmpty()).isTrue();
    }
}