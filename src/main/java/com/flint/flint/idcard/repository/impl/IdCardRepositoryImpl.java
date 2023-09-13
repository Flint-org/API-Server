package com.flint.flint.idcard.repository.impl;

import com.flint.flint.idcard.repository.custom.IdcardRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

/**
 * @author 정순원
 * @since 2023-09-10
 */
@RequiredArgsConstructor
public class IdCardRepositoryImpl implements IdcardRepositoryCustom {

    private final JPAQueryFactory queryFactory;


}
