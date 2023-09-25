package com.flint.flint.idcard.controller;

import com.flint.flint.common.ResponseForm;
import com.flint.flint.idcard.service.IdCardFolderService;
import com.flint.flint.security.auth.dto.AuthorityMemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 정순원
 * @since 2023-09-22
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/idcard/box/folder")
public class IdCardFolderGetController {

    private final IdCardFolderService idCardFolderService;

    @PreAuthorize("hasRole('ROLE_AUTHUSER')")
    @GetMapping("/{folderName}")
    public ResponseForm getIdCardFolder(@PathVariable String folderName,
                                          @AuthenticationPrincipal AuthorityMemberDTO memberDTO) {
        return new ResponseForm<>(idCardFolderService.getIdCardFolder(folderName, memberDTO));
    }
}
