package com.flint.flint.member.service;

import com.flint.flint.mail.dto.request.VeriyEmailAuthnumberRequest;
import com.flint.flint.member.domain.idcard.IdCard;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.repository.IdCardRepository;
import com.flint.flint.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdCardService {

    private final IdCardRepository idCardRepository;
    public void saveFrontIdCard(Member member, VeriyEmailAuthnumberRequest request) {
        IdCard idcard = IdCard.builder()
                .member(member)
                .email(request.getEmail())
                .admissionYear(request.getAdmissionYear())
                .university(request.getUniversity())
                .major(request.getMajor())
                .build();
        idCardRepository.save(idcard);
    }


}
