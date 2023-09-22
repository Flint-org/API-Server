package com.flint.flint.media.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.*;
import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.media.dto.response.PreSignedUrlResponse;
import com.flint.flint.media.dto.response.SavingFileResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.flint.flint.common.spec.ResultCode.*;

/**
 * 미디어 파일 처리 서비스
 *
 * @author 신승건
 * @since 2023-08-31
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MediaService {

    private final AmazonS3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    /**
     * Multipart 요청으로 온 파일들을 미디어 스토리지 서버에 저장하는 기능
     *
     * @param files      파일 목록
     * @param folderName 파일을 저장할 폴더 이름
     * @return 입력으로 들어온 파일들이 저장된 경로 리스트
     */
    public List<SavingFileResult> uploadFiles(List<MultipartFile> files, String folderName) {

        if (s3Client == null)
            throw new FlintCustomException(HttpStatus.FORBIDDEN, AWS_CREDENTIAL_FAIL);

        if (bucketName == null)
            throw new FlintCustomException(HttpStatus.FORBIDDEN, UNKNOWN_BUCKET_NAME);

        if (files.isEmpty())
            throw new FlintCustomException(HttpStatus.BAD_REQUEST, EMPTY_MEDIA_FILES);

        ObjectMetadata metadata = new ObjectMetadata();
        List<SavingFileResult> filePaths = new ArrayList<>(); // 저장 경로 Path URI 리스트

        for (int i = 0; i < files.size(); ++i) {
            String fileName = files.get(i).getOriginalFilename(); // 파일 이름 추출
            if (fileName != null) {

                // 유효한 이미지 포맷인지 검사
                if (!isValidImageExtension(fileName))
                    throw new FlintCustomException(HttpStatus.BAD_REQUEST, INVALID_IMAGE_EXTENSION_TYPE);

                String generatedFileName = generateFileName(fileName); // 새로운 고유 파일 이름 생성

                // 메타 데이터 설정
                metadata.setContentType(files.get(i).getContentType());
                metadata.setContentLength(files.get(i).getSize());

                // 저장할 경로 설정
                String path = folderName + "/" + generatedFileName;

                try {
                    // 저장 요청
                    s3Client.putObject(new PutObjectRequest(bucketName, path, files.get(i).getInputStream(), metadata));
                } catch (IOException e) {
                    log.error(e.getMessage());
                    throw new FlintCustomException(HttpStatus.BAD_REQUEST, MEDIA_FILE_SAVE_ERROR,
                            "미디어 파일 저장 요청에서 총 " + files.size() + "개의 파일 중" + (i + 1) + "번째 저장에서 실패하였습니다.");
                }
                filePaths.add(SavingFileResult.builder()
                        .originalFileName(fileName)
                        .savedURI(path)
                        .savedFullPath(s3Client.getUrl(bucketName, path).toString())
                        .build());
            } else {
                throw new FlintCustomException(HttpStatus.BAD_REQUEST, IMPOSSIBLE_EXTRACT_ORIGINAL_FILE_NAME);
            }
        }
        return filePaths;
    }

    /**
     * 미디어 파일을 삭제하는 요청
     *
     * @param filePath 삭제하고자 하는 파일이 저장된 경로
     */
    public void deleteFile(String filePath) {
        if (s3Client == null)
            throw new FlintCustomException(HttpStatus.FORBIDDEN, AWS_CREDENTIAL_FAIL);

        if (doesObjectExist(filePath)) {
            try {
                // 파일 삭제
                s3Client.deleteObject(new DeleteObjectRequest(bucketName, filePath));
            } catch (Exception e) {
                throw new FlintCustomException(HttpStatus.BAD_REQUEST, MEDIA_FILE_DELETE_ERROR);
            }
        }
    }

    /**
     * AWS S3로부터 Pre-Signed URL을 요청하는 메소드
     *
     * @param folderName 저장할 폴더 이름
     * @param fileName   저장할 파일 이름
     * @return Pre-Signed URL
     */
    public PreSignedUrlResponse getPreSignedURL(String folderName, String fileName) {
        if (!isValidImageExtension(fileName))
            throw new FlintCustomException(HttpStatus.BAD_REQUEST, INVALID_IMAGE_EXTENSION_TYPE);

        String path = folderName + "/" + fileName;

        URL url = s3Client.generatePresignedUrl(getGeneratePreSignedUrlRequest(path));
        return new PreSignedUrlResponse(url.toString());
    }

    /**
     * Pre-Signed URL 요청 데이터를 생성하는 메소드
     *
     * @param key 저장할 경로
     * @return 요청 객체
     */
    public GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String key) {
        GeneratePresignedUrlRequest preSignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key)
                .withMethod(HttpMethod.PUT)
                .withExpiration(getPreSignedUrlExpiration());

        preSignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString()
        );
        return preSignedUrlRequest;
    }

    /**
     * Pre-Signed URL의 만료 시간을 생성하는 메소드
     *
     * @return 만료 시간
     */
    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2; // 2분
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    /**
     * 주어진 경로에서 객체의 메타데이터 요청 시도함에 따라 존재 여부 판단
     *
     * @param filePath 검증할 파일 경로
     * @return 존재하면 true, 아니면 경우에 따라 exception 발생
     */
    public boolean doesObjectExist(String filePath) {
        try {
            // 주어진 경로에 대한 객체의 메타데이터 요청 시도
            s3Client.getObjectMetadata(bucketName, filePath);
            return true;
        } catch (AmazonServiceException e) {
            if (e.getStatusCode() == 404)
                throw new FlintCustomException(HttpStatus.NOT_FOUND, NOT_EXIST_OBJECT_GIVEN_PATH);
            else
                throw new FlintCustomException(HttpStatus.BAD_REQUEST, UNEXPECTED_AWS_SERVICE_ERROR);
        }
    }

    /**
     * 파일 이름 중복을 피하기 위해 파일 명을 변환하는 메소드
     *
     * @param fileName 확장자를 포함한 변환하고자 하는 파일명
     * @return uuid로 변환된 파일명
     */
    private String generateFileName(String fileName) {
        String extension = fileName.split("\\.")[1];
        return UUID.randomUUID() + "." + extension;
    }

    /**
     * 입력으로 들어온 파일명이 이미지 형식에 맞는지 안맞는지 검증하는 메소드
     *
     * @param fileName 확장자를 포함한 파일명
     * @return 이미지 포맷이 맞으면 true, 아닐 시 false
     */
    private boolean isValidImageExtension(String fileName) {
        String extension = fileName.split("\\.")[1];

        String[] validExtensions = {"jpeg", "jpg", "png", "gif", "bmp", "tiff", "webp", "svg", "ico"};

        for (String validExtension : validExtensions) {
            if (extension.equals(validExtension)) {
                return true;
            }
        }
        return false;
    }
}
