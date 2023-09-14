package com.flint.flint.idcard.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

public class IdCardFolderUpdateRequest {

    @Data
    @AllArgsConstructor
    public static class CreateFolderRequest {

        @NotBlank
        private String title;
    }

}
