package com.flint.flint.club.service.main;

import com.flint.flint.club.domain.main.*;
import com.flint.flint.club.domain.spec.ClubCategoryType;
import com.flint.flint.club.domain.spec.MemberJoinStatus;
import com.flint.flint.club.repository.main.*;
import com.flint.flint.club.request.ClubCreateRequest;
import com.flint.flint.club.request.PageRequest;
import com.flint.flint.club.response.ClubDetailGetResponse;
import com.flint.flint.club.response.ClubsGetResponse;
import com.flint.flint.common.exception.FlintCustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.flint.flint.common.spec.ResultCode.CLUB_NOT_FOUND_ERROR;

@RequiredArgsConstructor
@Service
public class ClubServiceImpl {
    private final ClubRepository clubRepository;
    private final ClubEnvironmentRepository clubEnvironmentRepository;
    private final ClubCategoryRepository clubCategoryRepository;
    private final ClubRequirementRepository clubRequirementRepository;
    private final MemberInClubRepository memberInClubRepository;

    /**
     * 모임 생성 서비스 메소드
     * @param clubCreateRequest : Reqeust Body 에 담아서 오는 모임 생성 DTO
     */
    @Transactional
    public void createClub(ClubCreateRequest clubCreateRequest) {
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

        // MemberInClub Entity
        // todo : Member 넣어주기, Auth 에 맞춰서 status 바꿔주기
        MemberInClub memberInClub = MemberInClub.builder()
                .club(getCreatedClub)
                .status(MemberJoinStatus.WAIT)
                .build();

        clubEnvironmentRepository.save(createdClubEnvironment);
        clubRequirementRepository.save(createdClubRequirement);
        clubCategoryRepository.save(createdClubCategory);
        memberInClubRepository.save(memberInClub);
    }

    public Page<Club> getClubs(ClubCategoryType clubCategoryType, String sortProperties, String direction) {
        PageRequest pageRequest = PageRequest.builder()
                .sortProperties(sortProperties)
                .direct(direction)
                .build();

        Pageable pageable = pageRequest.of(pageRequest.getSortProperties(), pageRequest.getDirection());

        return clubRepository.getClubsByPaging(clubCategoryType, pageable);
    }

    public ClubDetailGetResponse getClubDetail(Long clubId) {

        return null;
    }

}
