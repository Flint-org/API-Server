package com.flint.flint.idcard.repository;

import com.flint.flint.idcard.domain.IdCardFolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdCardFolderJPARepository extends JpaRepository<IdCardFolder, Long> {
}
