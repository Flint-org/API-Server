package com.flint.flint.member.repository;

import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.domain.main.UniversityCertify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UniversityCertifyRepository extends JpaRepository<UniversityCertify, Long> {
    Optional<UniversityCertify> findByMember(Member member);
}
