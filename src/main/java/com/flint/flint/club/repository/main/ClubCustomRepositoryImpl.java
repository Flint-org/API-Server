package com.flint.flint.club.repository.main;

import com.flint.flint.club.domain.main.*;
import com.flint.flint.club.domain.spec.ClubCategoryType;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

import static com.flint.flint.club.domain.main.QClub.club;
import static com.flint.flint.club.domain.main.QClubCategory.clubCategory;
import static com.flint.flint.club.domain.main.QMemberInClub.memberInClub;
import static com.querydsl.core.types.dsl.Wildcard.count;

@Slf4j
public class ClubCustomRepositoryImpl implements ClubCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public ClubCustomRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    // todo: club Entity 를 DTO로 번경하여 보내기
    public Page<Club> getClubsByPaging(ClubCategoryType clubCategoryType, Pageable pageable) {
        List<Club> results = getClubsByCategory(clubCategoryType, pageable);

        List<Tuple> fetch = jpaQueryFactory.select(club, clubCategory)
                .from(club)
                .innerJoin(clubCategory).on(club.id.eq(clubCategory.id))
                .where(clubCategory.categoryType.eq(clubCategoryType))
                .fetch();

        List<Club> collect = fetch.stream().map(tuple -> tuple.get(club))
                .toList();


        return new PageImpl<>(results, pageable, collect.size());
    }

    // 카테고리에 맞는 Club 전체 조회
    private List<Club> getClubsByCategory(ClubCategoryType clubCategoryType, Pageable pageable) {

        List<Tuple> fetch = jpaQueryFactory.select(club, clubCategory)
                .from(club)
                .innerJoin(clubCategory).on(club.id.eq(clubCategory.id))
                .where(clubCategory.categoryType.eq(clubCategoryType))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(clubsSort(pageable))
                .fetch();

        return fetch.stream()
                .map(tuple -> tuple.get(club))
                .toList();
    }

    private OrderSpecifier<?> clubsSort(Pageable pageable) {
        OrderSpecifier orderDirection = null;
        if(!pageable.getSort().isEmpty()) {
            for (Sort.Order order: pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                orderDirection = getOrderSpecifier(order.getProperty(), direction);
            }
        }
        return orderDirection;
    }

    private OrderSpecifier getOrderSpecifier(String property, Order direction) {
        switch (property) {
            case "최신순":
                log.info("최신 순으로 정렬");
                return new OrderSpecifier(direction, club.createdDate);
            case "인기순":
                log.info("인기 순으로 정렬");
                return new OrderSpecifier(direction, memberInClub.club.count());
        }
        return null;
    }

}
