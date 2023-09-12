package com.flint.flint.idcard.service;

import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.idcard.domain.IdCard;
import com.flint.flint.idcard.dto.request.IdCardRequest;
import com.flint.flint.idcard.dto.response.IdCardGetResponse;
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
import static org.junit.jupiter.api.Assertions.*;

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
        Member member2 = Member.builder()
                .providerName("kakao")
                .email("test2@test.com")
                .providerId("test2")
                .authority(Authority.ANAUTHUSER)
                .build();

        memberRepository.save(member);
        memberRepository.save(member2);

        IdCard idCard = IdCard.builder()
                .member(member)
                .admissionYear(2020)
                .major("소프트웨어학부")
                .university("숭실대")
                .build();

        idCardJPARepository.save(idCard);

    }

    @Test
    @DisplayName("카드 뒷면 수정 테스트")
    void updateBackIdCard() {
        //given
        List<InterestType> list = new ArrayList<>();
        list.add(InterestType.GAME);
        list.add(InterestType.MOVIE);
        IdCardRequest.updateBackReqeust updateBackReqeust = IdCardRequest.updateBackReqeust.builder()
                .cardBackIntroduction("안녕하세요")
                .cardBackSNSId("io.onl")
                .cardBackMBTI("ENTJ")
                .cardBackInterestTypeList(list)
                .build();
        Long idCardId = 1L;

        //when
        idCardService.updateBackIdCard(idCardId, updateBackReqeust);

        //then
        IdCard idCard = idCardJPARepository.findById(1L).orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, IDCARD_NOT_FOUND));
        assertEquals(1L, idCard.getId());
        assertEquals("안녕하세요", idCard.getCardBackIntroduction());
        assertEquals("io.onl", idCard.getCardBackSNSId());
        assertEquals("ENTJ", idCard.getCardBackMBTI());
        assertEquals(list, idCard.getCardBackInterestTypeList());
    }

    @Test
    @DisplayName("자신 명함 조회 테스트")
    void findByMemberId() {
        //given
        Long memberId = 1L;

        //when
        IdCardGetResponse.MyIdCard response = idCardService.findMyIdCardByMemberId(memberId);

        //then
        assertEquals(response.getId(), 1L);
        assertEquals(response.getAdmissionYear(), 2020);
        assertEquals(response.getMajor(),"소프트웨어학부");
        assertEquals(response.getUniversity(), "숭실대");
        assertEquals(response.getCardBackIntroduction(), null);
        assertEquals(response.getCardBackMBTI(), null);
        assertEquals(response.getCardBackSNSId(), null);
        assertEquals(response.getCardBackInterestTypeList(), null);
    }

    @Test
    @DisplayName("인증하지 않은 유저가 조회 예외테스트")
    void findByMemberIdException() {
        //given
        Long member2Id = 2L;

        //when, Then
        assertThrows(FlintCustomException.class, () -> idCardService.findMyIdCardByMemberId(member2Id)) ;


    }
}