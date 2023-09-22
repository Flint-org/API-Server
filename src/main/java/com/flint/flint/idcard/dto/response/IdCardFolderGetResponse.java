package com.flint.flint.idcard.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class IdCardFolderGetResponse {

    private List<IdCardGetResponse.GetIdCard> idCardList;
}
