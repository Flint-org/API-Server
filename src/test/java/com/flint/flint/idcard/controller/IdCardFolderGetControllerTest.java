package com.flint.flint.idcard.controller;

import com.flint.flint.asset.repository.UniversityAssetRepository;
import com.flint.flint.custom_member.WithMockCustomMember;
import com.flint.flint.idcard.domain.IdCardFolder;
import com.flint.flint.idcard.repository.IdCardBoxJPARepository;
import com.flint.flint.idcard.repository.IdCardFolderJPARepository;
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
class IdCardFolderGetControllerTest {

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
    @Autowired
    IdCardFolderJPARepository folderJPARepository;


    @BeforeEach
    void init() {
        Member member1 = Member.builder()
                .name("정순원")
                .authority(Authority.AUTHUSER)
                .email("test@test.com")
                .providerName("kakao")
                .providerId("kakao test")
                .build();

        memberRepository.save(member1);

        IdCardFolder idCardFolder = IdCardFolder.builder()
                .title("운동")
                .idCard(null)     //폴더 종류만 조회할 때 idcard가 null 조회
                .member(member1)
                .build();
        folderJPARepository.save(idCardFolder);
    }

    @Test
    @DisplayName("특정 명함 폴더 조회 컨트롤러 테스트")
    @Transactional
    @WithMockCustomMember
    void getIdCardFolder() throws Exception {
        mockMvc.perform(get("/api/v1/idcard/box/folder/운동")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
}