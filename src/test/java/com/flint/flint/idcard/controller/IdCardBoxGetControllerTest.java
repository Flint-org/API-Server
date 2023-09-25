package com.flint.flint.idcard.controller;

import com.flint.flint.asset.domain.UniversityAsset;
import com.flint.flint.asset.repository.UniversityAssetRepository;
import com.flint.flint.custom_member.WithMockCustomMember;
import com.flint.flint.idcard.domain.IdCard;
import com.flint.flint.idcard.domain.IdCardBox;
import com.flint.flint.idcard.repository.IdCardBoxJPARepository;
import com.flint.flint.idcard.repository.IdCardJPARepository;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.repository.MemberRepository;
import com.flint.flint.member.spec.Authority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class IdCardBoxGetControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    IdCardJPARepository idCardJPARepository;
    @Autowired
    IdCardBoxJPARepository idCardBoxJPARepository;
    @Autowired
    UniversityAssetRepository assetRepository;

    @BeforeEach
    void init() {
        Member member1 = Member.builder()
                .name("정순원")
                .authority(Authority.AUTHUSER)
                .email("test@test.com")
                .providerName("kakao")
                .providerId("kakao test")
                .build();

        Member member2 = Member.builder()
                .name("김기현")
                .authority(Authority.AUTHUSER)
                .email("test2@test")
                .providerName("naver")
                .providerId("test2")
                .build();

        Member member3 = Member.builder()
                .name("신승건")
                .authority(Authority.AUTHUSER)
                .email("test3@test")
                .providerName("naver")
                .providerId("test3")
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);


        IdCard idcard1 = IdCard.builder()
                .member(member1)
                .major("소프트웨어학부")
                .university("가천대학교")
                .admissionYear(2020)
                .build();

        IdCard idcard2 = IdCard.builder()
                .member(member2)
                .major("소프트웨어학부")
                .university("가천대학교")
                .admissionYear(2018)
                .build();

        IdCard idcard3 = IdCard.builder()
                .member(member3)
                .major("소프트웨어학부")
                .university("가천대학교")
                .admissionYear(2019)
                .build();

        idCardJPARepository.save(idcard1);
        idCardJPARepository.save(idcard2);
        idCardJPARepository.save(idcard3);

        IdCardBox idCardBox2 = IdCardBox.builder()
                .member(member1)
                .idCard(idcard2)
                .build();
        IdCardBox idCardBox3 = IdCardBox.builder()
                .member(member1)
                .idCard(idcard3)
                .build();                         //정순원이 김기현, 신승건 명함을 가짐

        idCardBoxJPARepository.save(idCardBox2);
        idCardBoxJPARepository.save(idCardBox3);

        UniversityAsset asset = UniversityAsset.builder()
                .universityName("가천대학교")
                .emailSuffix("gachon.ac.kr")
                .red(8)
                .green(56)
                .blue(136)
                .logoUrl("/가천")
                .build();

        assetRepository.save(asset);


    }


    @Test
    @DisplayName("명함 박스 전체 조회 컨트롤러 테스트")
    @Transactional
    @WithMockCustomMember(role = "ROLE_AUTHUSER")
    void test1() throws Exception {
        mockMvc.perform(get("/api/v1/idcard/box")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
}