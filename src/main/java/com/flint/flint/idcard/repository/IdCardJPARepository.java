package com.flint.flint.idcard.repository;

import com.flint.flint.idcard.domain.IdCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdCardJPARepository extends JpaRepository<IdCard, Long>, IdcardRepositoryCustom {
}
