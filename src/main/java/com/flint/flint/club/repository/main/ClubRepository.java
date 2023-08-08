package com.flint.flint.club.repository.main;

import com.flint.flint.club.domain.main.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
/**
 * Club Repository Interface Class
 * @author 김기현
 * @since 2023-08-03
 */
public interface ClubRepository extends JpaRepository<Club, UUID> {
}
