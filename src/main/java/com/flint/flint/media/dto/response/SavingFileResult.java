package com.flint.flint.media.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 미디어 파일 저장 완료 뒤 저장 정보 DTO
 *
 * @author 신승건
 * @since 2023-08-31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavingFileResult {
    private String originalFileName; // 원본 이미지 파일 이름
    private String savedFullPath; // 이미지 파일이 저장된 풀 URL
    private String savedURI; // 저장된 경로의 리소스 URI
}
