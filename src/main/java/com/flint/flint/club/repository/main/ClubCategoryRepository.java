package com.flint.flint.club.repository.main;

import com.flint.flint.club.domain.main.Club;
import com.flint.flint.club.domain.main.ClubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubCategoryRepository extends JpaRepository<ClubCategory, Long>, ClubCustomRepository {
    Optional<ClubCategory> findByClub(Club club);
}
