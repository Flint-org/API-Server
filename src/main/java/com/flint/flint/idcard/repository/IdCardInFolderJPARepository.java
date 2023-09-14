package com.flint.flint.idcard.repository;

import com.flint.flint.idcard.domain.IdCardFolder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdCardInFolderJPARepository extends JpaRepository<IdCardFolder, Long> {
}
