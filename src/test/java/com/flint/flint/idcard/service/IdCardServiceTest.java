package com.flint.flint.idcard.service;

import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.idcard.domain.IdCard;
import com.flint.flint.idcard.dto.request.IdCardRequest;
import com.flint.flint.idcard.repository.IdCardJPARepository;
import com.flint.flint.idcard.spec.InterestType;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.repository.MemberRepository;
import com.flint.flint.member.spec.Authority;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static com.flint.flint.common.spec.ResultCode.IDCARD_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class IdCardServiceTest {

    @Autowired
    IdCardService idCardService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    IdCardJPARepository idCardJPARepository;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .providerName("kakao")
                .email("test@test.com")
                .providerId("test")
                .authority(Authority.AUTHUSER)
                .build();

        memberRepository.save(member);

        IdCard idCard = IdCard.builder()
                .member(member)
                .admissionYear(2020)
                .major("test")
                .university("test")
                .build();

        idCardJPARepository.save(idCard);

    }

    @Test
    @DisplayName("카드 뒷면 수정 테스트")
    void updateBackIdCard() {
        List<InterestType> list = new ArrayList<>();
        list.add(InterestType.GAME);
        list.add(InterestType.MOVIE);
        IdCardRequest.updateBackReqeust updateBackReqeust = IdCardRequest.updateBackReqeust.builder()
                .idCardId(1L)
                .cardBackIntroduction("안녕하세요")
                .cardBackSNSId("io.onl")
                .cardBackMBTI("ENTJ")
                .cardBackInterestTypeList(list)
                .build();

        idCardService.updateBackIdCard(updateBackReqeust);
        IdCard idCard = idCardJPARepository.findById(updateBackReqeust.getIdCardId()).orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, IDCARD_NOT_FOUND));
        assertEquals(1L, idCard.getId());
        assertEquals("안녕하세요", idCard.getCardBackIntroduction());
        assertEquals("io.onl", idCard.getCardBackSNSId());
        assertEquals("ENTJ", idCard.getCardBackMBTI());
        assertEquals(list, idCard.getCardBackInterestTypeList());
    }
}