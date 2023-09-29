package com.flint.flint.idcard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdCardBoxGetResponse {

    private List<IdCardGetResponse.GetIdCard> idCardList;
}
