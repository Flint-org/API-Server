package com.flint.flint.member.repository;

import com.flint.flint.member.domain.main.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @Author 정순원
 * @Since 2023-08-19
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
     Optional<Member> findByEmail(String memberEmail);

     boolean existsById(Long Long);

    Optional<Member> findByProviderId(String providerId);

    boolean existsByProviderId(String providerId);
}
