package com.flint.flint.idcard.service;

import com.flint.flint.idcard.domain.IdCard;
import com.flint.flint.idcard.domain.IdCardBox;
import com.flint.flint.idcard.repository.IdCardBoxJPARepository;
import com.flint.flint.idcard.repository.IdCardJPARepository;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdCardBoxService {

    private final IdCardBoxJPARepository idCardBoxJPARepository;
    private final IdCardJPARepository idCardJPARepository;
    private final MemberService memberService;
    private final IdCardService idCardService;
    public void saveIdCardBox(Long idCardId, Long memberId) {
        IdCard idCard = idCardService.getIdCard(idCardId);
        Member member = memberService.getMember(memberId);

        IdCardBox idCardBox = IdCardBox.builder()
                .idcard(idCard)
                .member(member)
                .build();

        idCardBoxJPARepository.save(idCardBox);
    }
}
