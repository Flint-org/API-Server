package com.flint.flint.member.service;

import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.common.spec.ResultCode;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.flint.flint.common.spec.ResultCode.*;

/**
 * @author 정순원
 * @since 2023-09-10
 */
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));
    }
}
