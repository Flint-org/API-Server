package com.flint.flint.idcard.service;

import com.flint.flint.idcard.domain.IdCardFolder;
import com.flint.flint.idcard.dto.response.IdCardFolderUpdateResponse;
import com.flint.flint.idcard.repository.IdCardFolderJPARepository;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.service.MemberService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class IdCardFolderServiceTest {
    @InjectMocks
    IdCardFolderService idCardFolderService;

    @Mock
    IdCardFolderJPARepository folderJPARepository;
    @Mock
    MemberService memberService;
    @Mock
    IdCardFolderUpdateResponse.CreateIdCardFolderResponse createIdCardFolderResponse;

    @Test
    @DisplayName("명함폴더 생성 서비스 테스트")
    void createIdCardFolderTest(){
        //given
        String title = "Test Folder";
        Long memberId = 1L;
        Member member = new Member();

        given(memberService.getMember(memberId)).willReturn(member);
        given(folderJPARepository.save(any(IdCardFolder.class))).willReturn(mock(IdCardFolder.class));

        // when
        idCardFolderService.createIdCardFolder(title, memberId);

        // then
        then(folderJPARepository).should().save(any(IdCardFolder.class));
    }
}