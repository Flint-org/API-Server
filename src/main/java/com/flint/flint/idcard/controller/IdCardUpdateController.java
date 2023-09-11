package com.flint.flint.idcard.controller;

import com.flint.flint.common.ResponseForm;
import com.flint.flint.idcard.dto.request.IdCardRequest;
import com.flint.flint.idcard.service.IdCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/idcard")
@RequiredArgsConstructor
public class IdCardUpdateController {

    private final IdCardService idCardService;

    @PostMapping("/update")
    public ResponseForm updateIdCardBack(@Valid @RequestBody IdCardRequest.updateBackReqeust backReqeust) {
        idCardService.saveOrUpdateBackIdCard(backReqeust);
        return new ResponseForm<>();
    }
}
