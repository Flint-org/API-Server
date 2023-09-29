package com.flint.flint.idcard.repository;

import com.flint.flint.idcard.domain.IdCardFolder;
import com.flint.flint.member.domain.main.Member;
import jakarta.mail.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
 * @author 정순원
 * @since 2023-09-22
 */
public interface IdCardFolderJPARepository extends JpaRepository<IdCardFolder, Long> {

}
