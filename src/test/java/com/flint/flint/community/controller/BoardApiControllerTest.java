package com.flint.flint.community.controller;

import com.flint.flint.community.domain.board.Board;
import com.flint.flint.community.domain.board.BoardBookmark;
import com.flint.flint.community.repository.BoardBookmarkRepository;
import com.flint.flint.community.repository.BoardRepository;
import com.flint.flint.community.spec.BoardType;
import com.flint.flint.custom_member.WithMockCustomMember;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private BoardBookmarkRepository boardBookmarkRepository;
    @Autowired
    private MemberRepository memberRepository;
    private static final String BASE_URL = "/api/v1/boards";

    @Test
    @DisplayName("학교 인증을 받은 회원이 게시판 즐겨찾기에 등록하면 성공한다.")
    @WithMockCustomMember
    void registerBoardBookmark() throws Exception {
        Board board = Board.builder()
                .boardType(BoardType.GENERAL)
                .generalBoardName("자유게시판")
                .build();

        Board savedBoard = boardRepository.save(board);

        Member member = Member.builder()
                .name("테스터")
                .email("test@test.com")
                .providerName("kakao")
                .providerId("kakao test")
                .build();

        memberRepository.save(member);

        this.mockMvc.perform(post(BASE_URL + "/{boardId}/bookmark", savedBoard.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("학교 인증을 받은 회원이 즐겨찾기된 게시판을 즐겨찾기 해제하면 성공한다.")
    @WithMockCustomMember
    void deleteBoardBookmark() throws Exception {
        Board board = Board.builder()
                .boardType(BoardType.GENERAL)
                .generalBoardName("자유게시판")
                .build();

        Board savedBoard = boardRepository.save(board);

        Member member = Member.builder()
                .name("테스터")
                .email("test@test.com")
                .providerName("kakao")
                .providerId("kakao test")
                .build();

        memberRepository.save(member);

        BoardBookmark bookmark = BoardBookmark.builder()
                .board(savedBoard)
                .member(member)
                .build();
        boardBookmarkRepository.save(bookmark);

        this.mockMvc.perform(delete(BASE_URL + "/{boardId}/bookmark", savedBoard.getId()))
                .andExpect(status().isOk());
    }
}