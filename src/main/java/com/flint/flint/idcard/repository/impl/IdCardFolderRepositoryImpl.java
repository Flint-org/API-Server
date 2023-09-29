package com.flint.flint.idcard.repository.impl;

import com.flint.flint.idcard.domain.IdCard;
import com.flint.flint.idcard.repository.custom.IdCardFolderRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.flint.flint.idcard.domain.QIdCardFolder.idCardFolder;


/**
 * @author 정순원
 * @since 2023-09-22
 */
@Repository
@RequiredArgsConstructor
public class IdCardFolderRepositoryImpl implements IdCardFolderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

     public List<IdCard> findIdCardByTitleAndMember(String title, Long memberId) {
        return queryFactory.select(idCardFolder.idCard)
                .from(idCardFolder)
                .where(idCardFolder.title.eq(title),
                        idCardFolder.member.id.eq(memberId))
                .fetch();
    }

}
