package com.flint.flint.asset.controller;

import com.flint.flint.asset.dto.LogoInfoResponse;
import com.flint.flint.asset.dto.MajorsResponse;
import com.flint.flint.asset.dto.UniversityEmailResponse;
import com.flint.flint.asset.service.AssetService;
import com.flint.flint.common.ResponseForm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 학교 자료 컨트롤러
 *
 * @author 신승건
 * @since 2023-09-11
 */
@RestController
@RequestMapping("/api/v1/assets")
@RequiredArgsConstructor
public class AssetApiController {
    private final AssetService assetService;


    /**
     * 학교 로고 정보 조회 API
     *
     * @param universityName 학교 이름
     */
    @GetMapping("/logo/{universityName}")
    public ResponseForm<LogoInfoResponse> getLogoInformation(
            @PathVariable("universityName") String universityName
    ) {
        return new ResponseForm<>(assetService.getUniversityLogoInfoByName(universityName));
    }

    /**
     * 학교 이메일 정보 조회 API
     *
     * @param universityName 학교 이름
     */
    @GetMapping("/email/{universityName}")
    public ResponseForm<UniversityEmailResponse> getEmailInformation(
            @PathVariable("universityName") String universityName
    ) {
        return new ResponseForm<>(assetService.getUniversityEmailByName(universityName));
    }

    /**
     * 전체 학교 목록 조회 API
     */
    @GetMapping("/universities")
    public ResponseForm<List<String>> getUniversities() {
        return new ResponseForm<>(assetService.getUniversities());
    }

    /**
     * 학교의 전체 전공 대분류 목록 조회
     *
     * @param universityName 학교 이름
     */
    @GetMapping("/majors/large-class/{universityName}")
    public ResponseForm<List<String>> getLargeClasses(
            @PathVariable("universityName") String universityName
    ) {
        return new ResponseForm<>(assetService.getLargeClassesByName(universityName));
    }

    /**
     * 특정 학교와 특정 대분류에 해당하는 전공 목록 조회
     *
     * @param universityName 학교 이름
     * @param largeClass     대분류 이름
     */
    @GetMapping("/majors")
    public ResponseForm<MajorsResponse> getMajors(
            @RequestParam(value = "universityName") String universityName,
            @RequestParam(value = "largeClass") String largeClass
    ) {
        return new ResponseForm<>(assetService.getMajorsByClassAndName(universityName, largeClass));
    }
}
