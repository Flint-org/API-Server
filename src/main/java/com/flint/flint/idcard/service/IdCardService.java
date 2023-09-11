package com.flint.flint.idcard.service;

import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.common.spec.ResultCode;
import com.flint.flint.idcard.dto.request.IdCardRequest;
import com.flint.flint.idcard.spec.InterestType;
import com.flint.flint.mail.dto.request.SuccessUniversityAuthRequest;
import com.flint.flint.idcard.domain.IdCard;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.idcard.repository.IdCardJPARepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 명함 서비스
 * @author 정순원
 * @since 2023-09-10
 */
@Service
@RequiredArgsConstructor
public class IdCardService {

    private final IdCardJPARepository idCardJPARepository;

    @Transactional
    public void saveFrontIdCard(Member member, SuccessUniversityAuthRequest request) {
        IdCard idcard = IdCard.builder()
                .member(member)
                .admissionYear(request.getAdmissionYear())
                .university(request.getUniversity())
                .major(request.getMajor())
                .build();
        idCardJPARepository.save(idcard);
    }

    @Transactional
    public void updateBackIdCard(IdCardRequest.updateBackReqeust request) {
        Long idCardId = request.getIdCardId();
        String cardBackMBTI = request.getCardBackMBTI();
        String cardBackSNSId = request.getCardBackSNSId();
        List<InterestType> cardBackInterestTypeList = request.getCardBackInterestTypeList();
        String cardBackIntroduction = request.getCardBackIntroduction();

        IdCard idCard = getIdCard(idCardId);
        idCard.updateBack(cardBackIntroduction, cardBackMBTI, cardBackSNSId, cardBackInterestTypeList);
    }

    private IdCard getIdCard(Long idCardId) {
        return idCardJPARepository.findById(idCardId).orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, ResultCode.IDCARD_NOT_FOUND));
    }
}
