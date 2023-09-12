package com.flint.flint.idcard.service;

import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.common.spec.ResultCode;
import com.flint.flint.idcard.domain.IdCard;
import com.flint.flint.idcard.dto.request.IdCardRequest;
import com.flint.flint.idcard.dto.response.IdCardGetResponse;
import com.flint.flint.idcard.repository.IdCardJPARepository;
import com.flint.flint.idcard.spec.InterestType;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.repository.MemberRepository;
import com.flint.flint.member.spec.Authority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.flint.flint.common.spec.ResultCode.IDCARD_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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
    @Transactional
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
        Member member = memberRepository.findByEmail("test@test.com").orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, ResultCode.USER_NOT_FOUND));
        IdCard idCard = idCardJPARepository.findByMember(member).orElseThrow(()-> new FlintCustomException(HttpStatus.NOT_FOUND, IDCARD_NOT_FOUND));

        //when
        idCardService.updateBackIdCard(idCard.getId(), updateBackReqeust);

        //then
        assertEquals(updateBackReqeust.getCardBackIntroduction(), idCard.getCardBackIntroduction());
        assertEquals(updateBackReqeust.getCardBackSNSId(), idCard.getCardBackSNSId());
        assertEquals(updateBackReqeust.getCardBackMBTI(), idCard.getCardBackMBTI());
        assertEquals(list, idCard.getCardBackInterestTypeList());
    }

    @Test
    @DisplayName("자신 명함 조회 테스트")
    @Transactional
    void findByMemberId() {
        //given
        Member member = memberRepository.findByEmail("test@test.com").orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, ResultCode.USER_NOT_FOUND));
        IdCard idCard = idCardJPARepository.findByMember(member).orElseThrow(()-> new FlintCustomException(HttpStatus.NOT_FOUND, IDCARD_NOT_FOUND));

        //when
        IdCardGetResponse.MyIdCard response = idCardService.findMyIdCardByMemberId(member.getId());

        //then
        assertEquals(response.getId(), idCard.getId());
        assertEquals(response.getAdmissionYear(), idCard.getAdmissionYear());
        assertEquals(response.getMajor(),idCard.getMajor());
        assertEquals(response.getUniversity(), idCard.getUniversity());
        assertEquals(response.getCardBackIntroduction(), idCard.getCardBackIntroduction());
        assertEquals(response.getCardBackMBTI(), idCard.getCardBackMBTI());
        assertEquals(response.getCardBackSNSId(), idCard.getCardBackSNSId());
        assertEquals(response.getCardBackInterestTypeList(), idCard.getCardBackInterestTypeList());
    }

    @Test
    @DisplayName("인증하지 않은 유저 조회 예외테스트")
    @Transactional
    void findByMemberIdException() {
        //given
        Member member2 = memberRepository.findByEmail("test2@test.com").orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, ResultCode.USER_NOT_FOUND));

        //when, Then
        assertThrows(FlintCustomException.class, () -> idCardService.findMyIdCardByMemberId(member2.getId())) ;
    }
}