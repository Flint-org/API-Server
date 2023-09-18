package com.flint.flint.idcard.repository;

import com.flint.flint.idcard.domain.IdCard;
import com.flint.flint.idcard.domain.IdCardBox;
import com.flint.flint.member.domain.main.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdCardBoxJPARepository extends JpaRepository<IdCardBox, Long> {

    void deleteByMemberAndIdCard(Member member, IdCard idCard);


}
