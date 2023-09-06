package com.flint.flint.member.service;

import com.flint.flint.mail.dto.request.SuccessUniversityAuthRequest;
import com.flint.flint.member.domain.idcard.IdCard;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.repository.IdCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdCardService {

    private final IdCardRepository idCardRepository;

    public void saveFrontIdCard(Member member, SuccessUniversityAuthRequest request) {
        IdCard idcard = IdCard.builder()
                .member(member)
                .admissionYear(request.getAdmissionYear())
                .university(request.getUniversity())
                .major(request.getMajor())
                .build();
        idCardRepository.save(idcard);
    }


}
