package com.flint.flint.idcard.controller;

import com.flint.flint.common.ResponseForm;
import com.flint.flint.idcard.dto.request.IdCardRequest;
import com.flint.flint.idcard.service.IdCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author 정순원
 * @since 2023-09-10
 */
@RestController
@RequestMapping("/api/v1/idcard")
@RequiredArgsConstructor
public class IdCardUpdateController {

    private final IdCardService idCardService;

    @PutMapping("/back/{idCardId}")
    public ResponseForm updateIdCardBack(@PathVariable Long idCardId, @Valid @RequestBody IdCardRequest.updateBackReqeust backReqeust) {
        idCardService.updateBackIdCard(idCardId, backReqeust);
        return new ResponseForm<>();
    }
}
