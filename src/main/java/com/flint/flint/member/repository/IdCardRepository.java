package com.flint.flint.member.repository;

import com.flint.flint.member.domain.idcard.IdCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdCardRepository extends JpaRepository<IdCard, Long> {
}
