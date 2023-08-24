package com.flint.flint.security.auth;


import com.flint.flint.security.auth.jwt.JwtService;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.domain.main.Policy;
import com.flint.flint.member.repository.MemberRepository;
import com.flint.flint.member.repository.PolicyRepository;
import com.flint.flint.redis.RedisUtil;
import com.flint.flint.security.auth.dto.AuthenticationResponse;
import com.flint.flint.security.auth.dto.ClaimsDTO;
import com.flint.flint.security.auth.dto.RegisterRequest;
import com.flint.flint.security.oauth.dto.OAuth2UserAttribute;
import com.flint.flint.security.oauth.dto.OAuth2AccessToken;
import com.flint.flint.security.oauth.dto.OAuth2UserAttributeFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 로그인, 회원가입, 토큰 재발급에 관한 서비스
 * @Author 정순원
 * @Since 2023-08-19
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final PolicyRepository policyRepository;
    private final RedisUtil redisUtil;


    /**
     * 회원가입
     * member 저장, 수신동의 저장, 엑세스,리프레쉬토큰 생성, redis에 리프레쉬 토큰 저장
     */
    @Transactional
    public AuthenticationResponse register(RegisterRequest registerRequest, OAuth2AccessToken oAuth2AccessToken) {
        //카카오인지 네이버인지 선택
        OAuth2UserAttribute oAuth2UserAttribute = OAuth2UserAttributeFactory.of(registerRequest.getProviderName());
        //정보 추출
        oAuth2UserAttribute.UserAttributesByOAuthToken(oAuth2AccessToken);
        Member member = oAuth2UserAttribute.toEntity();
        Policy policy = Policy.builder().
                member(member).
                registerRequest(registerRequest).
                build();
        memberRepository.save(member);
        policyRepository.save(policy);
        //토큰에 넣을 Claim들을 전달할 DTO 생성
        ClaimsDTO claimsDTO = ClaimsDTO.from(member);
        //토큰 생성
        String accessToken = jwtService.generateAccessToken(claimsDTO);
        String refreshToken = jwtService.generateRefreshToken(claimsDTO);
        String providerId = jwtService.parseProviderId(refreshToken);
        //redis에 리프레쉬 토큰 저장
        redisUtil.save(refreshToken, providerId);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * 로그인
     * 엑세스,리프레쉬토큰 생성, redis에 리프레쉬 토큰 저장
     */
    @Transactional
    public AuthenticationResponse login(String providerName, OAuth2AccessToken oAuth2AccessToken) {
        OAuth2UserAttribute oAuth2UserAttribute = OAuth2UserAttributeFactory.of(providerName);
        oAuth2UserAttribute.UserAttributesByOAuthToken(oAuth2AccessToken);
        String providerId = oAuth2UserAttribute.getProviderId();
        Member member = memberRepository.findByProviderId(providerId).orElseThrow();
        ClaimsDTO claimsDTO = ClaimsDTO.from(member);
        String accessToken = jwtService.generateAccessToken(claimsDTO);
        String refreshToken = jwtService.generateRefreshToken(claimsDTO);
        redisUtil.save(refreshToken, providerId);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * 리프레쉬 토큰 재발급
     */
    @Transactional
    public AuthenticationResponse newTokenByRefreshToken(String refreshToken) {
        String providerId = jwtService.parseProviderId(refreshToken);
        Member member = memberRepository.findByEmail(providerId).orElseThrow();
        if (jwtService.isTokenValid(refreshToken)) {
            ClaimsDTO claimsDTO = ClaimsDTO.from(member);
            String newAccessToken = jwtService.generateAccessToken(claimsDTO);
            String newRefreshToken = jwtService.generateRefreshToken(claimsDTO);
            redisUtil.save(newRefreshToken, providerId);
            return AuthenticationResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        }
        return null;
    }
}
