package com.flint.flint.idcard.repository.impl;

import com.flint.flint.idcard.domain.IdCard;
import com.flint.flint.idcard.repository.custom.IdCardBoxRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.flint.flint.idcard.domain.QIdCardBox.idCardBox;

/**
 * @author 정순원
 * @since 2023-09-22
 */
@Repository
@RequiredArgsConstructor
public class IdCardBoxRepositoryImpl implements IdCardBoxRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<IdCard> findIdCardListByMemberId(Long memberId) {
        return queryFactory
                .select(idCardBox.idCard)
                .from(idCardBox)
                .where(idCardBox.member.id.eq(memberId))
                .fetch();
    }
}
