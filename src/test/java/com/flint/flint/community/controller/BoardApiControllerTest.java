package com.flint.flint.community.controller;

import com.flint.flint.community.domain.board.Board;
import com.flint.flint.community.domain.board.BoardBookmark;
import com.flint.flint.community.domain.board.MajorBoard;
import com.flint.flint.community.repository.BoardBookmarkRepository;
import com.flint.flint.community.repository.BoardRepository;
import com.flint.flint.community.repository.MajorBoardRepository;
import com.flint.flint.community.spec.BoardType;
import com.flint.flint.custom_member.WithMockCustomMember;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class BoardApiControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MajorBoardRepository majorBoardRepository;
    @Autowired
    private BoardBookmarkRepository boardBookmarkRepository;
    @Autowired
    private MemberRepository memberRepository;
    private static final String BASE_URL = "/api/v1/boards";

    @BeforeEach
    void init() {
        Board board = Board.builder()
                .boardType(BoardType.GENERAL)
                .generalBoardName("자유게시판")
                .build();

        Board board2 = Board.builder()
                .boardType(BoardType.GENERAL)
                .generalBoardName("새내기게시판")
                .build();

        Board majorBoard1 = Board.builder().boardType(BoardType.MAJOR)
                .generalBoardName(null)
                .build();

        Board majorBoard2 = Board.builder().boardType(BoardType.MAJOR)
                .generalBoardName(null)
                .build();

        MajorBoard major1 = MajorBoard.builder()
                .board(majorBoard1)
                .name("공학계열")
                .upperMajorBoard(null)
                .build();
        boardRepository.save(board);
        boardRepository.save(board2);
        boardRepository.save(majorBoard1);
        boardRepository.save(majorBoard2);

        MajorBoard savedMajor1 = majorBoardRepository.save(major1);
        MajorBoard lower11 = MajorBoard.builder()
                .board(majorBoard2)
                .name("건축")
                .upperMajorBoard(savedMajor1)
                .lowerMajorBoards(null)
                .build();

        List<MajorBoard> major1s = new ArrayList<>();

        major1s.add(lower11);
        major1.updateLowerMajorBoards(major1s);
    }

    @Test
    @DisplayName("학교 인증을 받은 회원이 게시판 즐겨찾기에 등록하면 성공한다.")
    @WithMockCustomMember
    void registerBoardBookmark() throws Exception {
        Member member = Member.builder()
                .name("테스터")
                .email("test@test.com")
                .providerName("kakao")
                .providerId("kakao test")
                .build();

        memberRepository.save(member);

        Board board = boardRepository.findBoardByGeneralBoardName("자유게시판").get();
        this.mockMvc.perform(post(BASE_URL + "/{boardId}/bookmark", board.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("학교 인증을 받은 회원이 즐겨찾기된 게시판을 즐겨찾기 해제하면 성공한다.")
    @WithMockCustomMember
    void deleteBoardBookmark() throws Exception {
        Member member = Member.builder()
                .name("테스터")
                .email("test@test.com")
                .providerName("kakao")
                .providerId("kakao test")
                .build();

        memberRepository.save(member);

        Board board = boardRepository.findBoardByGeneralBoardName("자유게시판").get();

        BoardBookmark bookmark = BoardBookmark.builder()
                .board(board)
                .member(member)
                .build();
        boardBookmarkRepository.save(bookmark);

        this.mockMvc.perform(delete(BASE_URL + "/{boardId}/bookmark", board.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("학교 인증을 받은 회원이 즐겨찾기된 게시판을 즐겨찾기 해제하면 성공한다.")
    @WithMockCustomMember
    void getBoardBookmarkList() throws Exception {
        Member member = Member.builder()
                .name("테스터")
                .email("test@test.com")
                .providerName("kakao")
                .providerId("kakao test")
                .build();

        memberRepository.save(member);

        Board board = boardRepository.findBoardByGeneralBoardName("자유게시판").get();
        Board board2 = boardRepository.findBoardByMajorName("건축").get();

        BoardBookmark bookmark = BoardBookmark.builder()
                .board(board)
                .member(member)
                .build();

        BoardBookmark bookmark2 = BoardBookmark.builder()
                .board(board2)
                .member(member)
                .build();
        boardBookmarkRepository.save(bookmark);
        boardBookmarkRepository.save(bookmark2);

        this.mockMvc.perform(get(BASE_URL + "/bookmark"))
                .andExpect(status().isOk());
    }
}