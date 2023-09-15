package com.flint.flint.idcard.service;

import com.flint.flint.idcard.domain.IdCard;
import com.flint.flint.idcard.domain.IdCardBox;
import com.flint.flint.idcard.repository.IdCardBoxJPARepository;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.service.MemberService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class IdCardBoxServiceTest {

    @InjectMocks
    IdCardBoxService idCardBoxService;

    @Mock
    IdCardBoxJPARepository idCardBoxJPARepository;

    @Mock
    IdCardService idCardService;

    @Mock
    MemberService memberService;

    /**
     * BDD Mockito
     */
    @Test
    @DisplayName("명함박스 생성 서비스 테스트")
    void createIdCardBoxTest() {
        //given
        Long idCardId = 1L;
        Long memberId = 1L;
        IdCard idCard = new IdCard();
        Member member = new Member();

        given(idCardService.getIdCard(idCardId)).willReturn(idCard); //Mockito.when()에 해당
        given(memberService.getMember(memberId)).willReturn(member);

        // when
        idCardBoxService.createIdCardBox(idCardId, memberId);

        // then
        then(idCardBoxJPARepository).should().save(any(IdCardBox.class));
    }

    @Test
    @DisplayName("명함박스 생성 서비스 테스트")
    void removeIdCardBox() {
        //given
        Long idCardId = 1L;
        Long memberId = 1L;
        IdCard idCard = new IdCard();
        Member member = new Member();

        given(idCardService.getIdCard(idCardId)).willReturn(idCard); //Mockito.when()에 해당
        given(memberService.getMember(memberId)).willReturn(member);

        //when
        idCardBoxService.removeIdCardBox(idCardId, memberId);


        //then
        then(idCardBoxJPARepository).should().deleteByMemberAndIdCard(member,idCard);
    }
}