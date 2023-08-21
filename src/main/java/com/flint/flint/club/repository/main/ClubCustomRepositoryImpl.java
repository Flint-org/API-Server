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

import static com.flint.flint.club.domain.main.QClub.club;
import static com.flint.flint.club.domain.main.QClubCategory.clubCategory;
import static com.flint.flint.club.domain.main.QMemberInClub.memberInClub;

@Slf4j
public class ClubCustomRepositoryImpl implements ClubCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public ClubCustomRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Club> getClubsByPaging(ClubCategoryType clubCategoryType, Pageable pageable) {
        List<Club> results = getClubsByCategory(clubCategoryType, pageable);

        Long count = jpaQueryFactory
                .select(club.count())
                .from(club, clubCategory)
                .leftJoin(club).on(clubCategory.club.eq(club))
                .where(clubCategory.categoryType.eq(clubCategoryType))
                .fetchOne();

        return new PageImpl<>(results, pageable, count);
    }

    // 카테고리에 맞는 Club 전체 조회
    public List<Club> getClubsByCategory(ClubCategoryType clubCategoryType, Pageable pageable) {

        return jpaQueryFactory.select(club)
                .from(club, clubCategory)
                .leftJoin(club).on(clubCategory.club.eq(club))
                .where(clubCategory.categoryType.eq(clubCategoryType))
                .orderBy(clubsSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
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
