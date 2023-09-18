package com.flint.flint.idcard.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

public class IdCardFolderUpdateResponse {

    @Data
    @AllArgsConstructor
    public static class CreateIdCardFolderResponse {
        private long folderId;
    }
}
