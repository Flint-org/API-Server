package com.flint.flint.idcard.service;

import com.flint.flint.asset.dto.LogoInfoResponse;
import com.flint.flint.asset.service.AssetService;
import com.flint.flint.idcard.domain.IdCard;
import com.flint.flint.idcard.domain.IdCardBox;
import com.flint.flint.idcard.dto.response.IdCardBoxGetResponse;
import com.flint.flint.idcard.dto.response.IdCardGetResponse;
import com.flint.flint.idcard.repository.IdCardBoxJPARepository;
import com.flint.flint.idcard.repository.custom.IdCardBoxRepositoryCustom;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.service.MemberService;
import com.flint.flint.security.auth.dto.AuthorityMemberDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IdCardBoxService {

    private final IdCardBoxJPARepository idCardBoxJPARepository;
    private final IdCardBoxRepositoryCustom idCardBoxRepositoryCustom;
    private final MemberService memberService;
    private final IdCardService idCardService;
    private final AssetService assetService;

    @Transactional
    public void createIdCardBox(Long idCardId, Long memberId) {
        IdCard idCard = idCardService.getIdCard(idCardId);
        Member member = memberService.getMember(memberId);

        IdCardBox idCardBox = IdCardBox.builder()
                .idCard(idCard)
                .member(member)
                .build();

        idCardBoxJPARepository.save(idCardBox);
    }

    @Transactional
    public void removeIdCardBox(Long idCardId, Long memberId) {
        IdCard idCard = idCardService.getIdCard(idCardId);
        Member member = memberService.getMember(memberId);

        idCardBoxJPARepository.deleteByMemberAndIdCard(member, idCard);
    }

    @Transactional
    public IdCardBoxGetResponse getIdCardBox(AuthorityMemberDTO memberDTO) {
        List<IdCard> idCardList = idCardBoxRepositoryCustom.findIdCardListByMemberId(memberDTO.getId());
        IdCardBoxGetResponse idCardBoxGetResponse = new IdCardBoxGetResponse();

        idCardList.stream()
                .map(idCard -> {
                    LogoInfoResponse univInfo = assetService.getUniversityLogoInfoByName(idCard.getUniversity()); //학교 생상정보 추출
                    return IdCardGetResponse.GetIdCard.of(univInfo, idCard);  //학교 색상정보와 idcard 담는 리스폰스 생성
                })
                .forEach(idCardResponse -> idCardBoxGetResponse.getIdCardList().add(idCardResponse));
        return idCardBoxGetResponse;
    }
}
