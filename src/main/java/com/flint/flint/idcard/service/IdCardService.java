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
    public void saveOrUpdateBackIdCard(IdCardRequest.updateBackReqeust reqeust) {
        Long idCardId = reqeust.getIdCardId();
        String cardBackMBTI = reqeust.getCardBackMBTI();
        String cardBackSNSId = reqeust.getCardBackSNSId();
        List<InterestType> cardBackInterestTypeList = reqeust.getCardBackInterestTypeList();
        String cardBackIntroduction = reqeust.getCardBackIntroduction();

        IdCard idCard = getIdCard(idCardId);
        idCard.UpdateBack(cardBackIntroduction, cardBackMBTI, cardBackSNSId, cardBackInterestTypeList);
        idCardJPARepository.save(idCard);
    }

    private IdCard getIdCard(Long idCardId) {
        return idCardJPARepository.findById(idCardId).orElseThrow(() -> new FlintCustomException(HttpStatus.BAD_REQUEST, ResultCode.IDCARD_NOT_FOUND));
    }
}
