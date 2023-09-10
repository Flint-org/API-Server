package com.flint.flint.club.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flint.flint.club.controller.main.ClubController;
import com.flint.flint.club.domain.spec.*;
import com.flint.flint.club.repository.main.ClubRepository;
import com.flint.flint.club.request.ClubCreateRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ClubControllerTest {
    @Autowired
    ClubController clubController;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ClubRepository clubRepository;

    @BeforeEach
    void clear() {
        clubRepository.deleteAll();
    }

    private final String BASE_URL = "/api";

    @Test
    @DisplayName("모임 생성 컨트롤러 테스트")
    void test1() throws Exception{

        String requestBody = objectMapper.writeValueAsString(
                ClubCreateRequest.builder()
                        .categoryType(ClubCategoryType.RUNNING)
                        .frequencyType(ClubFrequency.REGULAR)
                        .name("러닝 모임 모집")
                        .description("러닝 모임을 모집합니다 많은 참여 부탁드립니다")
                        .rule("규칙은 다음과 같습니다")
                        .meetingStartDate(LocalDate.parse("2023-09-09"))
                        .meetingEndDate(LocalDate.parse("2023-10-10"))
                        .meetingType(ClubMeetingType.OFFLINE)
                        .location("성남시")
                        .day(LocalDate.parse("2023-09-25").getDayOfWeek())
                        .joinType(ClubJoinRequirement.AUTH_JOIN)
                        .grade("3.8")
                        .memberLimitCount(5)
                        .genderType(ClubGenderRequirement.NONE)
                        .build()
        );

        mockMvc.perform(post(BASE_URL + "/v1/clubs")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }
}
