package com.flint.flint.idcard.repository;

import com.flint.flint.idcard.domain.IdCard;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 정순원
 * @since 2023-09-10
 */
public interface IdCardJPARepository extends JpaRepository<IdCard, Long>, IdcardRepositoryCustom {
}
