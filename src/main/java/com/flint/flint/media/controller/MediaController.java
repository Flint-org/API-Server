package com.flint.flint.media.controller;

import com.flint.flint.common.ResponseForm;
import com.flint.flint.media.dto.request.PreSignedUrlRequest;
import com.flint.flint.media.dto.response.PreSignedUrlResponse;
import com.flint.flint.media.service.MediaService;
import com.flint.flint.media.dto.response.SavingFileResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 미디어 파일 컨트롤러
 *
 * @author 신승건
 * @since 2023-08-31
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/medias")
public class MediaController {

    private final MediaService mediaService;

    /**
     * 파일을 입력으로 받아서 파일을 업로드하는 핸들러
     *
     * @param files      요청받은 파일 목록
     * @param folderName 파일들을 저장할 폴더 이름
     * @return 요청받은 파일들이 저장된 경로 정보들
     */
    @PostMapping(value = "", consumes = {"multipart/form-data"})
    public ResponseForm<List<SavingFileResult>> uploadFiles(
            @RequestPart(value = "medias", required = false) List<MultipartFile> files,
            @RequestPart(value = "folderName") String folderName
    ) {
        return new ResponseForm<>(mediaService.uploadFiles(files, folderName));
    }

    /**
     * 미디어 파일 업로드를 위한 Pre-Signed URL을 요청하는 핸들러
     *
     * @param preSignedUrlRequest 미디어 파일 업로드할 폴더와 파일명
     * @return 생성된 Pre-Signed URL
     */
    @PostMapping("/pre-signed-url")
    public ResponseForm<PreSignedUrlResponse> generatePreSignedUrl(
            @RequestBody PreSignedUrlRequest preSignedUrlRequest
    ) {
        return new ResponseForm<>(mediaService.getPreSignedURL(preSignedUrlRequest.getFolderName(), preSignedUrlRequest.getFileName()));
    }

    /**
     * 주어진 파일 경로에 객체(파일)이 존재하는지 검증하는 핸들러
     *
     * @param filePath 검증하고자 하는 파일 경로
     * @return 존재하면 정상처리, 그렇지 않으면 클라이언트 에러
     */
    @PostMapping("/check-exist/{filePath}")
    public ResponseForm<Void> checkExistObject(
            @PathVariable("filePath") String filePath
    ) {
        mediaService.doesObjectExist(filePath);
        return new ResponseForm<>();
    }
}
