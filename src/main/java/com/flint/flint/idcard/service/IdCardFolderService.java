package com.flint.flint.idcard.service;

import com.flint.flint.asset.dto.LogoInfoResponse;
import com.flint.flint.asset.service.AssetService;
import com.flint.flint.idcard.domain.IdCard;
import com.flint.flint.idcard.domain.IdCardFolder;
import com.flint.flint.idcard.dto.response.IdCardFolderGetResponse;
import com.flint.flint.idcard.dto.response.IdCardFolderUpdateResponse;
import com.flint.flint.idcard.dto.response.IdCardGetResponse;
import com.flint.flint.idcard.repository.IdCardFolderJPARepository;
import com.flint.flint.idcard.repository.custom.IdCardFolderRepositoryCustom;
import com.flint.flint.member.service.MemberService;
import com.flint.flint.security.auth.dto.AuthorityMemberDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author 정순원
 * @since 2023-09-22
 */
@Service
@RequiredArgsConstructor
public class IdCardFolderService {


    private final IdCardFolderJPARepository folderJPARepository;
    private final MemberService memberService;
    private final IdCardFolderRepositoryCustom idCardFolderRepositoryCustom;
    private final AssetService assetService;

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

    @Transactional
    public IdCardFolderGetResponse getIdCardFolder(String folderName, AuthorityMemberDTO memberDTO) {
        List<IdCard> idCardList = idCardFolderRepositoryCustom.findIdCardByTitleAndMember(folderName, memberDTO.getId());
        IdCardFolderGetResponse idCardFolderGetResponse =  new IdCardFolderGetResponse(new ArrayList<IdCardGetResponse.GetIdCard>());

        idCardList.stream()
                .map(idCard -> {
                    LogoInfoResponse univInfo = assetService.getUniversityLogoInfoByName(idCard.getUniversity()); //학교 생상정보 추출
                    return IdCardGetResponse.GetIdCard.of(univInfo, idCard);  //학교 색상정보와 idcard 담는 리스폰스 생성
                })
                .forEach(idCardResponse -> idCardFolderGetResponse.getIdCardList().add(idCardResponse));
        return idCardFolderGetResponse;
    }
}
