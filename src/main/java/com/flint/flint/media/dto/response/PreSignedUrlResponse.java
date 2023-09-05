package com.flint.flint.media.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 클라이언트에서 직접 파일 업로드를 하기 위한 생성한 pre-signed url
 *
 * @author 신승건
 * @since 2023-09-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreSignedUrlResponse {
    private String preSignedUrl;
}
