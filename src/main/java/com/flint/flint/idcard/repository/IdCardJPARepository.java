package com.flint.flint.idcard.repository;

import com.flint.flint.idcard.domain.IdCard;
import com.flint.flint.idcard.repository.custom.IdcardRepositoryCustom;
import com.flint.flint.member.domain.main.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author 정순원
 * @since 2023-09-10
 */
public interface IdCardJPARepository extends JpaRepository<IdCard, Long>, IdcardRepositoryCustom {

    Optional<IdCard> findByMember(Member member);
}
