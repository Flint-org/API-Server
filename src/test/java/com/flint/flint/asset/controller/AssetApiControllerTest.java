package com.flint.flint.asset.controller;

import com.flint.flint.asset.domain.UniversityAsset;
import com.flint.flint.asset.domain.UniversityMajor;
import com.flint.flint.asset.repository.UniversityAssetRepository;
import com.flint.flint.asset.repository.UniversityMajorRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AssetApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UniversityAssetRepository assetRepository;
    @Autowired
    private UniversityMajorRepository majorRepository;
    private static final String BASE_URL = "/api/v1/assets";

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

    @Test
    @DisplayName("학교 이름으로 학교 로고 정보를 조회한다.")
    void getUniversityLogo() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(BASE_URL + "/logo/{universityName}", "가천대학교"))
                .andExpect(status().isOk())
                .andReturn();

        JSONObject data = extractPayload(mvcResult);

        assertEquals("/가천", data.optString("logoUrl"));
        assertEquals(8, data.optInt("red"));
        assertEquals(56, data.optInt("green"));
        assertEquals(136, data.optInt("blue"));
    }

    @Test
    @DisplayName("유효하지 않은 대학교 이름으로 로고 조회 시 예외가 발생한다.")
    void getLogoWithException() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                        get(BASE_URL + "/logo/{universityName}", "뚫꿇대학교")
                ).andExpect(status().is4xxClientError())
                .andReturn();

        JSONObject status = extractStatus(mvcResult);

        assertEquals("F700", status.optString("resultCode"));
    }

    @Test
    @DisplayName("학교 이름으로 학교 이메일 정보를 조회한다.")
    void getUniversityEmail() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(BASE_URL + "/email/{universityName}", "가천대학교"))
                .andExpect(status().isOk())
                .andReturn();

        JSONObject data = extractPayload(mvcResult);

        assertEquals("gachon.ac.kr", data.optString("emailSuffix"));
    }

    @Test
    @DisplayName("전체 학교 목록을 조회한다.")
    void getUniversities() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(BASE_URL + "/universities"))
                .andExpect(status().isOk())
                .andReturn();

        JSONObject json = new JSONObject(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
        JSONArray jsonArray = json.optJSONArray("data");
        assertEquals(4, jsonArray.length());
    }

    @Test
    @DisplayName("학교 이름으로 해당 학교의 전공 대분류 목록을 중복 없이 조회한다.")
    void getLargeClasses() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(BASE_URL + "/majors/large-class/{universityName}", "가천대학교"))
                .andExpect(status().isOk())
                .andReturn();

        JSONObject json = new JSONObject(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
        JSONArray jsonArray = json.optJSONArray("data");

        assertEquals(2, jsonArray.length());
        assertEquals("공학계열", jsonArray.getString(0));
        assertEquals("인문사회계열", jsonArray.getString(1));
    }

    @Test
    @DisplayName("학교 이름과 전공 대분류로 전공 이름 기준 오름차순으로 전공 목록을 조회한다.")
    void getMajors() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(BASE_URL + "/majors")
                        .param("universityName", "가천대학교")
                        .param("largeClass", "공학계열"))
                .andExpect(status().isOk())
                .andReturn();

        JSONObject data = extractPayload(mvcResult);

        assertEquals(3, data.optJSONArray("majors").length());
        assertEquals("신소재공학과", data.optJSONArray("majors").getString(0));
    }

    private JSONObject extractStatus(MvcResult mvcResult) throws Exception {
        JSONObject json = new JSONObject(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
        return json.optJSONObject("statusResponse");
    }

    private JSONObject extractPayload(MvcResult mvcResult) throws Exception {
        JSONObject json = new JSONObject(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
        return json.optJSONObject("data");
    }
}