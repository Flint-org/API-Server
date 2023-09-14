package com.flint.flint.idcard.service;

import com.flint.flint.idcard.domain.IdCardFolder;
import com.flint.flint.idcard.dto.response.IdCardFolderUpdateResponse;
import com.flint.flint.idcard.repository.IdCardInFolderJPARepository;
import com.flint.flint.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdCardFolderService {

    private final IdCardInFolderJPARepository FolderJPARepository;
    private final MemberService memberService;
    private final IdCardService idCardService;

    @Transactional
    public IdCardFolderUpdateResponse.CreateIdCardFolderResponse createIdCardFolder(String title, Long memberId) {

        IdCardFolder idcardFolder = IdCardFolder.builder()
                .title(title)
                .idcard(null)     //폴더 종류만 조회할 때 idcard가 null 조회
                .member(memberService.getMember(memberId))
                .build();

        IdCardFolder saveIdCardFolder = FolderJPARepository.save(idcardFolder);

        return new IdCardFolderUpdateResponse.CreateIdCardFolderResponse(saveIdCardFolder.getId());
    }
}
