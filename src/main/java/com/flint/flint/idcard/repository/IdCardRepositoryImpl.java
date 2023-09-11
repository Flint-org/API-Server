package com.flint.flint.idcard.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IdCardRepositoryImpl implements IdcardRepositoryCustom {

    private final JPAQueryFactory queryFactory;


}
