package com.flint.flint.idcard.repository.custom;

import com.flint.flint.idcard.domain.IdCard;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 정순원
 * @since 2023-09-22
 */
@Repository
public interface IdCardFolderRepositoryCustom {

    public List<IdCard> findIdCardByTitleAndMember(String title, Long memberId);
}
