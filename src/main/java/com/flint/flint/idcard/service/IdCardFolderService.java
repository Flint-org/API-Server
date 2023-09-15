package com.flint.flint.idcard.service;

import com.flint.flint.idcard.domain.IdCardFolder;
import com.flint.flint.idcard.dto.response.IdCardFolderUpdateResponse;
import com.flint.flint.idcard.repository.IdCardFolderJPARepository;
import com.flint.flint.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdCardFolderService {


    private final IdCardFolderJPARepository folderJPARepository;
    private final MemberService memberService;
    private final IdCardService idCardService;


    @Transactional
    public IdCardFolderUpdateResponse.CreateIdCardFolderResponse createIdCardFolder(String title, Long memberId) {

        IdCardFolder idCardFolder = IdCardFolder.builder()
                .title(title)
                .idCard(null)     //폴더 종류만 조회할 때 idcard가 null 조회
                .member(memberService.getMember(memberId))
                .build();

        IdCardFolder saveIdCardFolder = folderJPARepository.save(idCardFolder);

        return new IdCardFolderUpdateResponse.CreateIdCardFolderResponse(saveIdCardFolder.getId());
    }
}
