package com.flint.flint.club.service.main;

import com.flint.flint.club.domain.main.Club;
import com.flint.flint.club.repository.main.ClubRepository;
import com.flint.flint.club.request.ClubCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClubServiceImpl {
    private final ClubRepository clubRepository;

    /**
     * 모임 생성 서비스 메소드
     * @param clubCreateRequest : Reqeust Body 에 담아서 오는 모임 생성 DTO
     */
    public void createService(ClubCreateRequest clubCreateRequest) {
        Club createdClub = clubCreateRequest.toEntity();
        clubRepository.save(createdClub);
        // todo: Environment, Default Image, Requirement, Category,
    }
}
