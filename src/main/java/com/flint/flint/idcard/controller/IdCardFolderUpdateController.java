package com.flint.flint.idcard.controller;

import com.flint.flint.common.ResponseForm;
import com.flint.flint.idcard.service.IdCardFolderService;
import com.flint.flint.security.auth.dto.AuthorityMemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 정순원
 * @since 2023-09-22
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/idcard/box/folder")
public class IdCardFolderUpdateController {

    private final IdCardFolderService idCardFolderService;

    @PreAuthorize("hasRole('ROLE_AUTHUSER')")
    @PostMapping("/{folderName}")
    public ResponseForm createIdCardFolder(@PathVariable String folderName,
                                           @AuthenticationPrincipal AuthorityMemberDTO memberDTO) {
        return new ResponseForm<>(idCardFolderService.createIdCardFolder(folderName, memberDTO.getId()));
    }
}
