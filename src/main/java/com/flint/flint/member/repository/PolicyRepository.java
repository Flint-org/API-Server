package com.flint.flint.member.repository;

import com.flint.flint.member.domain.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author 정순원
 * @Since 2023-08-23
 */
public interface PolicyRepository extends JpaRepository<Policy, Long> {
}
