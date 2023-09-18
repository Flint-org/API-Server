package com.flint.flint.idcard.controller;

import com.flint.flint.common.ResponseForm;
import com.flint.flint.idcard.dto.request.IdCardFolderUpdateRequest;
import com.flint.flint.idcard.service.IdCardFolderService;
import com.flint.flint.security.auth.dto.AuthorityMemberDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/idcard/box/folder")
public class IdCardFolderUpdateController {

    private final IdCardFolderService idCardFolderService;

    @PostMapping
    public ResponseForm createIdCardFolder (@Valid @RequestBody IdCardFolderUpdateRequest.CreateFolderRequest requestBody,
                                            @AuthenticationPrincipal AuthorityMemberDTO memberDTO) {
        return new ResponseForm<>(idCardFolderService.createIdCardFolder(requestBody.getTitle(), memberDTO.getId()));
    }
}
