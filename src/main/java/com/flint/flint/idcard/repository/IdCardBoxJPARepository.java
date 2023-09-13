package com.flint.flint.idcard.repository;

import com.flint.flint.idcard.domain.IdCardBox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdCardBoxJPARepository extends JpaRepository<IdCardBox, Long> {
}
