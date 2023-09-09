package com.flint.flint.asset.service;

import com.flint.flint.asset.domain.UniversityAsset;
import com.flint.flint.asset.domain.UniversityMajor;
import com.flint.flint.asset.dto.LogoInfoResponse;
import com.flint.flint.asset.dto.MajorsResponse;
import com.flint.flint.asset.dto.UniversityEmailResponse;
import com.flint.flint.asset.repository.UniversityAssetRepository;
import com.flint.flint.asset.repository.UniversityMajorRepository;
import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.common.spec.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AssetServiceTest {

    @Autowired
    private AssetService assetService;
    @Autowired
    private UniversityAssetRepository assetRepository;
    @Autowired
    private UniversityMajorRepository majorRepository;

    @BeforeEach
    void init() {
        UniversityAsset asset = UniversityAsset.builder()
                .universityName("가천대학교")
                .emailSuffix("gachon.ac.kr")
                .red(8)
                .green(56)
                .blue(136)
                .logoUrl("/가천")
                .build();

        UniversityAsset asset2 = UniversityAsset.builder()
                .universityName("중앙대학교")
                .emailSuffix("cau.ac.kr")
                .red(155)
                .green(44)
                .blue(53)
                .logoUrl("/중앙")
                .build();

        UniversityAsset asset3 = UniversityAsset.builder()
                .universityName("한양대학교")
                .emailSuffix("hanyang.ac.kr")
                .red(71)
                .green(47)
                .blue(145)
                .logoUrl("/한양")
                .build();

        UniversityAsset asset4 = UniversityAsset.builder()
                .universityName("한양대학교(ERICA)")
                .emailSuffix("hanyang.ac.kr")
                .red(4)
                .green(52)
                .blue(134)
                .logoUrl("/한양(ERICA)")
                .build();

        assetRepository.save(asset);
        assetRepository.save(asset2);
        assetRepository.save(asset3);
        assetRepository.save(asset4);


        UniversityMajor major = UniversityMajor.builder()
                .universityName("가천대학교")
                .largeClass("공학계열")
                .mediumClass("a")
                .smallClass("b")
                .majorName("전기공학과")
                .build();
        UniversityMajor major2 = UniversityMajor.builder()
                .universityName("가천대학교")
                .largeClass("공학계열")
                .mediumClass("a")
                .smallClass("b")
                .majorName("신소재공학과")
                .build();
        UniversityMajor major3 = UniversityMajor.builder()
                .universityName("가천대학교")
                .largeClass("공학계열")
                .mediumClass("a")
                .smallClass("b")
                .majorName("컴퓨터공학부(컴퓨터공학전공)")
                .build();

        UniversityMajor major4 = UniversityMajor.builder()
                .universityName("가천대학교")
                .largeClass("인문사회계열")
                .mediumClass("a")
                .smallClass("b")
                .majorName("영어교육과")
                .build();
        UniversityMajor major5 = UniversityMajor.builder()
                .universityName("가천대학교")
                .largeClass("인문사회계열")
                .mediumClass("a")
                .smallClass("b")
                .majorName("경영학전공")
                .build();
        UniversityMajor major6 = UniversityMajor.builder()
                .universityName("가천대학교")
                .largeClass("인문사회계열")
                .mediumClass("a")
                .smallClass("b")
                .majorName("사회복지학전공")
                .build();

        majorRepository.save(major);
        majorRepository.save(major2);
        majorRepository.save(major3);
        majorRepository.save(major4);
        majorRepository.save(major5);
        majorRepository.save(major6);
    }

    @DisplayName("학교 이름으로 로고 정보 조회 시 존재하지 않는 대학교로 검색하면 예외가 발생한다.")
    @Test
    void getLogoInfoWithWrongName() {
        // given
        String universityName = "뷁궬대학교";

        // when, then
        assertThatThrownBy(() -> assetService.getUniversityLogoInfoByName(universityName))
                .isInstanceOf(FlintCustomException.class)
                .hasMessage(ResultCode.NOT_FOUND_UNIVERSITY_NAME.getMessage());
    }

    @DisplayName("존재하는 학교 이름으로 로고 정보 조회 시 학교 로고와 관련된 정보 제공에 성공한다.")
    @Test
    void getLogoInfo() {
        // given
        String universityName = "가천대학교";

        // when
        LogoInfoResponse logoInfo = assetService.getUniversityLogoInfoByName(universityName);

        // then
        assertEquals(8, logoInfo.getRed());
        assertEquals(56, logoInfo.getGreen());
        assertEquals(136, logoInfo.getBlue());
        assertEquals("/가천", logoInfo.getLogoUrl());
    }

    @DisplayName("특정 학교 이름으로 학교 이메일 접미사 조회에 성공한다.")
    @Test
    void getEmailInfo() {
        // given
        String universityName = "중앙대학교";
        String universityName2 = "가천대학교";

        // when
        UniversityEmailResponse email = assetService.getUniversityEmailByName(universityName);
        UniversityEmailResponse email2 = assetService.getUniversityEmailByName(universityName2);

        // then
        assertEquals("cau.ac.kr", email.getEmailSuffix());
        assertEquals("gachon.ac.kr", email2.getEmailSuffix());
    }

    @DisplayName("모든 대학교 이름 목록 조회에 성공한다.")
    @Test
    void getUniversities() {
        // given, when
        List<String> universities = assetService.getUniversities();

        // then
        assertEquals(4, universities.size());
        assertEquals("한양대학교", universities.get(2));
    }

    @DisplayName("특정 학교로 모든 대분류 목록 조회에 성공한다.")
    @Test
    void getLargeClasses() {
        // given
        String universityName = "가천대학교";

        // when
        List<String> classes = assetService.getLargeClassesByName(universityName);

        // then
        assertEquals(2, classes.size());
        assertEquals("인문사회계열", classes.get(1));
    }

    @DisplayName("존재하지 않는 학교나 대분류로 검색할 경우 검색 결과가 없어 예외가 발생한다.")
    @Test
    void getMajorsWithNoExisting() {
        // given
        String universityName = "가천대학교";
        String class1 = "뚫꿇계열";

        // when, then
        assertThatThrownBy(() -> assetService.getMajorsByClassAndName(universityName, class1))
                .isInstanceOf(FlintCustomException.class)
                .hasMessage(ResultCode.EMPTY_MAJOR_SEARCH.getMessage());
    }

    @DisplayName("특정 학교와 대분류로 검색 시 오름차 순으로 전공 목록 조회에 성공한다.")
    @Test
    void getMajors() {
        // given
        String universityName = "가천대학교";
        String class1 = "공학계열";
        String class2 = "인문사회계열";

        // when
        MajorsResponse majors1 = assetService.getMajorsByClassAndName(universityName, class1);
        MajorsResponse majors2 = assetService.getMajorsByClassAndName(universityName, class2);

        // then
        assertEquals(3, majors1.getMajors().size());
        assertEquals(3, majors2.getMajors().size());

        assertEquals("전기공학과", majors1.getMajors().get(1));
        assertEquals("신소재공학과", majors1.getMajors().get(0));

        assertEquals("경영학전공", majors2.getMajors().get(0));
        assertEquals("영어교육과", majors2.getMajors().get(2));
    }
}