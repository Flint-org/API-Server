package com.flint.flint.idcard.controller;

import com.flint.flint.common.ResponseForm;
import com.flint.flint.idcard.service.IdCardBoxService;
import com.flint.flint.security.auth.dto.AuthorityMemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/idcard/box")
public class IdCardBoxUpdateController {

    private final IdCardBoxService idCardBoxService;

    @PostMapping("/{idCardId}")
    public ResponseForm createIdCardBox (@PathVariable Long idCardId, @AuthenticationPrincipal AuthorityMemberDTO memberDTO) {
        Long memberId = memberDTO.getId();
        idCardBoxService.createIdCardBox(idCardId, memberId);
        return new ResponseForm<>();
    }

    @DeleteMapping("/{idCardId}")
    public ResponseForm removeIdCardBox (@PathVariable Long idCardId, @AuthenticationPrincipal AuthorityMemberDTO memberDTO) {
        Long memberId = memberDTO.getId();
        idCardBoxService.removeIdCardBox(idCardId, memberId);
        return new ResponseForm<>();
    }
}
