package com.flint.flint.community.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private Long boardId;
    @NotBlank
    private String title;
    @NotBlank
    private String contents;
    private List<String> fileNames = new ArrayList<>();
}
