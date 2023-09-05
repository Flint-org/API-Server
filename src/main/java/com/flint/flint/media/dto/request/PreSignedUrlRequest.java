package com.flint.flint.media.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pre-Signed URL을 생성하기 위한 데이터 요청 dto
 *
 * @author 신승건
 * @since 2023-09-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreSignedUrlRequest {
    private String folderName;
    private String fileName;
}
