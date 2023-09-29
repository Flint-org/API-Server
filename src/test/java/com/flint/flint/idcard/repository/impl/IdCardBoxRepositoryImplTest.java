package com.flint.flint.idcard.repository.impl;

import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.common.spec.ResultCode;
import com.flint.flint.idcard.domain.IdCard;
import com.flint.flint.idcard.domain.IdCardBox;
import com.flint.flint.idcard.repository.IdCardBoxJPARepository;
import com.flint.flint.idcard.repository.IdCardJPARepository;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.repository.MemberRepository;
import com.flint.flint.member.spec.Authority;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class IdCardBoxRepositoryImplTest {


    @Autowired
    MemberRepository memberRepository;

    @Autowired
    IdCardJPARepository idCardJPARepository;

    @Autowired
    IdCardBoxJPARepository idCardBoxJPARepository;

    @Autowired
    IdCardBoxRepositoryImpl idCardBoxRepositoryImpl;

    @BeforeEach
    void setUp() {

        Member member1 = Member.builder()
                .name("정순원")
                .authority(Authority.AUTHUSER)
                .email("test@test")
                .providerName("kakao")
                .providerId("test")
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
                .university("숭실대학교")
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


    }

    @Test
    @DisplayName("명함박스 내에 전체 명함 조회 테스트")
    @Transactional
    void findAllInIdCardBox() {
        //given
        Member member = memberRepository.findByEmail("test@test").orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, ResultCode.USER_NOT_FOUND));

        //when
        List<IdCard> idCardList = idCardBoxRepositoryImpl.findIdCardListByMemberId(member.getId());

        //then
        assertEquals(2018, idCardList.get(0).getAdmissionYear());
        assertEquals(2019, idCardList.get(1).getAdmissionYear());


    }
}