package com.flint.flint.club.repository.main;

import com.flint.flint.club.domain.main.Club;
import com.flint.flint.club.domain.main.ClubCategory;
import com.flint.flint.club.domain.spec.ClubCategoryType;
import com.flint.flint.club.response.ClubsGetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClubCustomRepository {

    Page<Club> getClubsByPaging(ClubCategoryType clubCategoryType, Pageable pageable);
}
