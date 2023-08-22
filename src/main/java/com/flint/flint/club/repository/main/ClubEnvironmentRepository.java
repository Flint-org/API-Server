package com.flint.flint.club.repository.main;

import com.flint.flint.club.domain.main.Club;
import com.flint.flint.club.domain.main.ClubEnvironment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubEnvironmentRepository extends JpaRepository<ClubEnvironment, Long>, ClubCustomRepository {
    Optional<ClubEnvironment> findByClub(Club club);
}
