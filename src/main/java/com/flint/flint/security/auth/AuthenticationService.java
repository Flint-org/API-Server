package com.flint.flint.security.auth;


import com.flint.flint.security.auth.jwt.JwtService;
import com.flint.flint.member.domain.Member;
import com.flint.flint.member.domain.Policy;
import com.flint.flint.member.repository.MemberRepository;
import com.flint.flint.member.repository.PolicyRepository;
import com.flint.flint.member.spec.Agree;
import com.flint.flint.redis.RedisUtil;
import com.flint.flint.security.auth.dto.AuthenticationResponse;
import com.flint.flint.security.auth.dto.ClaimsDTO;
import com.flint.flint.security.auth.dto.RegisterRequest;
import com.flint.flint.security.oauth.dto.OAuth2UserAttribute;
import com.flint.flint.security.oauth.dto.OAuth2AccessToken;
import com.flint.flint.security.oauth.dto.OAuth2UserAttributeFactory;
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
     * member 저장, 수신동의 저장, 엑세스,리프레쉬토큰 생성, redis에 리프레쉬 토큰 저장
     */
    public AuthenticationResponse register(RegisterRequest registerRequest, OAuth2AccessToken oAuth2AccessToken) {
        String providerName = registerRequest.getProviderName();
        String serviceUsingAgree = registerRequest.getServiceUsingAgree();
        String personalInformationAgree = registerRequest.getPersonalInformationAgree();
        String marketingAgree = registerRequest.getMarketingAgree();
        //카카오인지 네이버인지 선택
        OAuth2UserAttribute oAuth2UserAttribute = OAuth2UserAttributeFactory.of(providerName);
        //정보 추출
        oAuth2UserAttribute.UserAttributesByOAuthToken(oAuth2AccessToken);
        Member member = oAuth2UserAttribute.toEntity();
        Policy policy = Policy.builder()
                .member(member)
                .personalInformationAgree(Agree.valueOf(personalInformationAgree))
                .serviceUsingAgree(Agree.valueOf(serviceUsingAgree))
                .marketingAgree(Agree.valueOf(marketingAgree))
                .build();
        memberRepository.save(member);
        policyRepository.save(policy);
        ClaimsDTO claimsDTO = ClaimsDTO.from(member);
        String accessToken = jwtService.generateAccessToken(claimsDTO);
        String refreshToken = jwtService.generateRefreshToken(claimsDTO);
        String providerId = jwtService.parseProviderId(refreshToken);
        redisUtil.save(refreshToken, providerId);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     엑세스,리프레쉬토큰 생성, redis에 리프레쉬 토큰 저장
     */
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
