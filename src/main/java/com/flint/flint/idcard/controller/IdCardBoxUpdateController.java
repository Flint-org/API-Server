package com.flint.flint.idcard.controller;

import com.flint.flint.common.ResponseForm;
import com.flint.flint.idcard.dto.request.IdCardRequest;
import com.flint.flint.idcard.service.IdCardBoxService;
import com.flint.flint.security.auth.dto.AuthorityMemberDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/idcard/box")
public class IdCardBoxUpdateController {

    private final IdCardBoxService idCardBoxService;

    @PostMapping("/other/{idCardId}")
    public ResponseForm saveIdCardBox (@PathVariable Long idCardId, @AuthenticationPrincipal AuthorityMemberDTO memberDTO) {
        Long memberId = memberDTO.getId();
        idCardBoxService.saveIdCardBox(idCardId, memberId);
        return new ResponseForm<>();
    }
}
