package com.flint.flint.security.auth.dto;

import com.flint.flint.member.domain.main.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

/**
 * 엑세스토큰 claim에 담길 정보들 넘겨주는 DTO
 * @Author 정순원
 * @Since 2023-08-19
 */
@Getter
@AllArgsConstructor(access = PRIVATE)
@Builder
public class ClaimsDTO {

    private final Long id;
    private final String providerId;
    private final String email;
    private final String authority;


    public static ClaimsDTO from(Member member) {
        return ClaimsDTO.builder()
                .id(member.getId())
                .providerId(member.getProviderId())
                .email(member.getEmail())
                .authority(member.getAuthority().getRole())
                .build();
    }
}
