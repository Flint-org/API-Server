package com.flint.flint.idcard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class IdCardFolderGetResponse {

    private List<IdCardGetResponse.GetIdCard> idCardList;
}
