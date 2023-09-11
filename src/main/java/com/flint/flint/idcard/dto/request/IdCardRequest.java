package com.flint.flint.idcard.dto.request;

import com.flint.flint.idcard.spec.InterestType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 정순원
 * @since 2023-09-10
 */
public class IdCardRequest {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class  updateBackReqeust {

        private String cardBackIntroduction;
        private String cardBackSNSId;
        private String cardBackMBTI;
        private List<InterestType> cardBackInterestTypeList;
    }
}
