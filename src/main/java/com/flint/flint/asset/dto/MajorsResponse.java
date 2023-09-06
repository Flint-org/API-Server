package com.flint.flint.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MajorsResponse {
    private String universityName;
    private String largeClasses;
    private List<String> majors;
}
