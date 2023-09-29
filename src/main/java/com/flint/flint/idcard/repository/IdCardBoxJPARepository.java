package com.flint.flint.idcard.repository;

import com.flint.flint.idcard.domain.IdCard;
import com.flint.flint.idcard.domain.IdCardBox;
import com.flint.flint.idcard.repository.custom.IdCardBoxRepositoryCustom;
import com.flint.flint.member.domain.main.Member;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author 정순원
 * @since 2023-09-22
 */
public interface IdCardBoxJPARepository extends JpaRepository<IdCardBox, Long> {

    void deleteByMemberAndIdCard(Member member, IdCard idCard);
}
