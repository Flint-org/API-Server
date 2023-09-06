package com.flint.flint.asset.service;

import com.flint.flint.asset.domain.UniversityAsset;
import com.flint.flint.asset.domain.UniversityMajor;
import com.flint.flint.asset.dto.LogoInfoResponse;
import com.flint.flint.asset.dto.MajorsResponse;
import com.flint.flint.asset.dto.UniversityEmailResponse;
import com.flint.flint.asset.repository.UniversityAssetRepository;
import com.flint.flint.asset.repository.UniversityMajorRepository;
import com.flint.flint.common.exception.FlintCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.flint.flint.common.spec.ResultCode.*;

/**
 * Asset 데이터를 다루는 서비스
 *
 * @author 신승건
 * @since 2023-09-03
 */
@Service
@RequiredArgsConstructor
public class AssetService {

    private final UniversityAssetRepository assetRepository;
    private final UniversityMajorRepository majorRepository;


    /**
     * 특정 대학교의 로고 정보를 조회
     *
     * @param univName 대학교 이름
     * @return 해당 대학교의 로고 이미지, RGB 값 정보가 담긴 dto
     */
    @Transactional(readOnly = true)
    public LogoInfoResponse getUniversityLogoInfoByName(String univName) {
        UniversityAsset asset = assetRepository.findUniversityAssetByUniversityName(univName)
                .orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, NOT_FOUND_UNIVERSITY_NAME));

        return LogoInfoResponse.builder()
                .universityName(asset.getUniversityName())
                .red(asset.getRed())
                .green(asset.getGreen())
                .blue(asset.getBlue())
                .logoUrl(asset.getLogoUrl())
                .build();
    }

    /**
     * 특정 대학교의 이메일 주소를 조회
     *
     * @param univName 대학교 이름
     * @return 이메일 정보를 담은 dto
     */
    @Transactional(readOnly = true)
    public UniversityEmailResponse getUniversityEmailByName(String univName) {
        UniversityAsset asset = assetRepository.findUniversityAssetByUniversityName(univName)
                .orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, NOT_FOUND_UNIVERSITY_NAME));

        return UniversityEmailResponse.builder()
                .universityName(asset.getUniversityName())
                .emailSuffix(asset.getEmailSuffix())
                .build();
    }


    /**
     * 모든 대학교 이름을 조회
     *
     * @return 중복 없는 대학교 목록
     */
    @Transactional(readOnly = true)
    public List<String> getUniversities() {
        return assetRepository.findAll().stream()
                .map(UniversityAsset::getUniversityName)
                .toList();
    }

    /**
     * 특정 대학교의 대분류 목록을 조회
     *
     * @param univName 대학교 이름
     * @return 중복 없는 모든 대분류 목록
     */
    @Transactional(readOnly = true)
    public List<String> getLargeClassesByName(String univName) {
        return majorRepository.findDistinctLargeClassesByUniversityName(univName)
                .orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, NOT_FOUND_UNIVERSITY_NAME));
    }

    /**
     * 특정 대학교와 특정 대분류에 해당하는 전공 목록을 조회
     *
     * @param univName   대학교 이름
     * @param largeClass 대분류 이름
     * @return 위 두개 필드 조건에 맞는 모든 전공 목록
     */
    @Transactional(readOnly = true)
    public MajorsResponse getMajorsByClassAndName(String univName, String largeClass) {
        List<UniversityMajor> majors = majorRepository.findUniversityMajorsByUniversityNameAndLargeClassOrderByMajorName(univName, largeClass)
                .orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, NOT_FOUND_UNIVERSITY_NAME));

        // 전공 검색 결과 없으면 예외 발생
        if (majors.isEmpty())
            throw new FlintCustomException(HttpStatus.NOT_FOUND, EMPTY_MAJOR_SEARCH);

        return MajorsResponse.builder()
                .universityName(univName)
                .largeClasses(largeClass)
                .majors(majors.stream()
                        .map(UniversityMajor::getMajorName)
                        .toList())
                .build();
    }
}
