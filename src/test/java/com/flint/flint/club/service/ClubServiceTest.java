package com.flint.flint.club.service;

import com.flint.flint.club.domain.main.Club;
import com.flint.flint.club.domain.main.ClubCategory;
import com.flint.flint.club.domain.main.ClubEnvironment;
import com.flint.flint.club.domain.main.ClubRequirement;
import com.flint.flint.club.domain.spec.*;
import com.flint.flint.club.repository.main.ClubCategoryRepository;
import com.flint.flint.club.repository.main.ClubEnvironmentRepository;
import com.flint.flint.club.repository.main.ClubRepository;
import com.flint.flint.club.repository.main.ClubRequirementRepository;
import com.flint.flint.club.request.ClubCreateRequest;
import com.flint.flint.club.request.PageRequest;
import com.flint.flint.club.service.main.ClubServiceImpl;
import com.querydsl.core.types.OrderSpecifier;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

@SpringBootTest
@TestMethodOrder(MethodOrderer.Random.class)
public class ClubServiceTest {
    @Autowired
    ClubRepository clubRepository;
    @Autowired
    ClubRequirementRepository clubRequirementRepository;
    @Autowired
    ClubEnvironmentRepository clubEnvironmentRepository;
    @Autowired
    ClubCategoryRepository clubCategoryRepository;
    @Autowired
    ClubServiceImpl clubService;

    @BeforeEach
    void init() {
        clubRepository.deleteAll();
    }

    @Test
    @DisplayName("모임 생성 서비스 테스트")
    @Transactional
    @Order(1)
    void test1() {
        // given
        for(int i = 1; i <= 10; i++) {
            ClubCreateRequest request = ClubCreateRequest.builder()
                    .categoryType(ClubCategoryType.RUNNING)
                    .frequencyType(ClubFrequency.REGULAR)
                    .name("러닝 모임 모집 " + i)
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
                    .build();

            // when
            clubService.createClub(request);
        }


        Club getClub = clubRepository.findAll().get(0);
        ClubEnvironment getClubEnvironment = clubEnvironmentRepository.findAll().get(0);
        ClubCategory getClubCategory = clubCategoryRepository.findAll().get(0);
        ClubRequirement getClubRequirement = clubRequirementRepository.findAll().get(0);

        // then
        assertEquals(getClub.getId(), getClubEnvironment.getClub().getId());
        assertEquals(getClub.getId(), getClubRequirement.getClub().getId());
        assertEquals(getClub.getId(), getClubCategory.getClub().getId());

        assertEquals("러닝 모임 모집 1", getClub.getName());
        assertEquals("Offline", getClub.getMeetingType().getType());

        assertEquals("MONDAY", getClubEnvironment.getDay().name());
        assertEquals("성남시", getClubEnvironment.getLocation());

        assertEquals("러닝", getClubCategory.getCategoryType().getCategoryName());
        assertEquals("운동", getClubCategory.getCategoryType().getUpperCategory().getCategoryName());

        assertEquals("Auth", getClubRequirement.getJoinType().getType());
        assertEquals(5, getClubRequirement.getMemberLimitCount());

    }

    @Test
    @DisplayName("카테고리에 맞게 Club list 가져오기")
    @Order(2)
    @Transactional
    void test2() throws Exception {
        // given
        for(int i = 1; i <= 10; i++) {
            ClubCreateRequest request = ClubCreateRequest.builder()
                    .categoryType(ClubCategoryType.RUNNING)
                    .frequencyType(ClubFrequency.REGULAR)
                    .name("러닝 모임 모집 " + i)
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
                    .build();

            // when
            clubService.createClub(request);
        }

        ClubCategoryType type = ClubCategoryType.RUNNING;
        String sortProperties = "최신순";
        String direction = "ascending";

        // when
        Page<Club> clubs = clubService.getClubs(type, sortProperties, direction);
        PageRequest pageRequest = PageRequest.builder()
                .direct(direction)
                .sortProperties(sortProperties)
                .build();
        Pageable pageable = pageRequest.of(pageRequest.getSortProperties(), pageRequest.getDirection());


        // then
        assertEquals(pageable.getSort(), clubs.getSort());
        assertEquals("러닝 모임 모집 1", clubs.getContent().get(0).getName());
        assertEquals(10, clubs.getTotalElements());
        assertEquals(2, clubs.getTotalPages());
    }

}
