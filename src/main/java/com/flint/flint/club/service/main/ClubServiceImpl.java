package com.flint.flint.club.service.main;

import com.flint.flint.club.domain.main.Club;
import com.flint.flint.club.domain.main.ClubCategory;
import com.flint.flint.club.domain.main.ClubEnvironment;
import com.flint.flint.club.domain.main.ClubRequirement;
import com.flint.flint.club.repository.main.ClubCategoryRepository;
import com.flint.flint.club.repository.main.ClubEnvironmentRepository;
import com.flint.flint.club.repository.main.ClubRepository;
import com.flint.flint.club.repository.main.ClubRequirementRepository;
import com.flint.flint.club.request.ClubCreateRequest;
import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.common.handler.GlobalExceptionHandler;
import com.flint.flint.common.spec.ResultCode;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.flint.flint.common.spec.ResultCode.CLUB_NOT_FOUND_ERROR;

@RequiredArgsConstructor
@Service
public class ClubServiceImpl {
    private final ClubRepository clubRepository;
    private final ClubEnvironmentRepository clubEnvironmentRepository;
    private final ClubCategoryRepository clubCategoryRepository;
    private final ClubRequirementRepository clubRequirementRepository;

    /**
     * 모임 생성 서비스 메소드
     * @param clubCreateRequest : Reqeust Body 에 담아서 오는 모임 생성 DTO
     */
    @Transactional
    public void createService(ClubCreateRequest clubCreateRequest) {
        Club createdClub = clubCreateRequest.toClubEntity();
        clubRepository.save(createdClub);

        // 저장한 Club 가져오기
        Club getCreatedClub = clubRepository.findById(createdClub.getId()).orElseThrow(
                () -> new FlintCustomException(HttpStatus.NOT_FOUND, CLUB_NOT_FOUND_ERROR));

        // Environment Entity
        ClubEnvironment createdClubEnvironment = clubCreateRequest.toClubEnvironmentEntity(getCreatedClub);

        // Requirement Entity
        ClubRequirement createdClubRequirement = clubCreateRequest.toClubRequirementEntity(getCreatedClub);

        // Category Entity
        ClubCategory createdClubCategory = clubCreateRequest.toClubCategoryEntity(getCreatedClub);

        clubEnvironmentRepository.save(createdClubEnvironment);
        clubRequirementRepository.save(createdClubRequirement);
        clubCategoryRepository.save(createdClubCategory);
    }
}
