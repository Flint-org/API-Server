package com.flint.flint.club.repository.main;

import com.flint.flint.club.domain.main.Club;
import com.flint.flint.club.domain.main.ClubRequirement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubRequirementRepository extends JpaRepository<ClubRequirement, Long>, ClubCustomRepository {
    Optional<ClubRequirement> findByClub(Club club);
}
