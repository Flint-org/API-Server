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
import com.flint.flint.security.oauth.dto.AuthorizionRequestHeader;
import com.flint.flint.security.oauth.dto.OAuth2UserAttributeFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 로그인, 회원가입, 토큰 재발급에 관한 서비스
 * @Author 정순원
 * @Since 2023-08-19
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${jwt.refreshTokenExpiration}")
    private long refreshTokenExpiration;

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final PolicyRepository policyRepository;
    private final RedisUtil redisUtil;


    /**
     * 회원가입
     * member 저장, 수신동의 저장, 엑세스,리프레쉬토큰 생성, redis에 리프레쉬 토큰 저장
     */
    @Transactional
    public AuthenticationResponse register(RegisterRequest registerRequest, AuthorizionRequestHeader authorizionRequestHeader) {
        //카카오인지 네이버인지 선택
        OAuth2UserAttribute oAuth2UserAttribute = OAuth2UserAttributeFactory.of(registerRequest.getProviderName());
        String oauth2AccessToekn = authorizionRequestHeader.getAccessToken().replace("Bearer ", "");
        //정보 추출
        oAuth2UserAttribute.setUserAttributesByOauthToken(oauth2AccessToekn);
        Member member = oAuth2UserAttribute.toEntity();
        Policy policy = Policy.builder().
                member(member).
                registerRequest(registerRequest).
                build();
        memberRepository.save(member);
        policyRepository.save(policy);
        return generateToken(member);
    }

    /**
     * 유저 리프레쉬 토큰의 만료기간까지 다 지났을 때 로그인
     * (리프레쉬 토큰이 살아있을 때 로그인은 newTokenByRefreshToken()를 호출한다)
     * 엑세스,리프레쉬토큰 생성, redis에 리프레쉬 토큰 저장
     */
    @Transactional
    public AuthenticationResponse login(String providerName, AuthorizionRequestHeader authorizionRequestHeader) {
        OAuth2UserAttribute oAuth2UserAttribute = OAuth2UserAttributeFactory.of(providerName);
        String oauth2AccessToekn = authorizionRequestHeader.getAccessToken().replace("Bearer ", "");
        //정보 추출
        oAuth2UserAttribute.setUserAttributesByOauthToken(oauth2AccessToekn);
        String providerId = oAuth2UserAttribute.getProviderId();
        Member member = memberRepository.findByProviderId(providerId).orElseThrow();
        return generateToken(member);
    }

    /**
     * 리프레쉬 토큰 재발급
     */
    @Transactional
    public AuthenticationResponse newTokenByRefreshToken(String refreshToken) {
        String providerId = jwtService.parseProviderId(refreshToken);
        Member member = memberRepository.findByEmail(providerId).orElseThrow();
        if (jwtService.isTokenValid(refreshToken)) {
            return generateToken(member);
        }
        return null;
    }

    /**
     * 엑세스 토큰 리프레쉬 토큰 생성, 레디쉬에 리프레쉬 토큰 저장
     */
    public AuthenticationResponse generateToken(Member member) {
        ClaimsDTO claimsDTO = ClaimsDTO.from(member);
        String providerId = claimsDTO.getProviderId();
        String accessToken = jwtService.generateAccessToken(claimsDTO);
        String refreshToken = jwtService.generateRefreshToken(claimsDTO);
        redisUtil.save(providerId, refreshToken, refreshTokenExpiration);

            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .accessTokenExpiration(jwtService.parseExpiration(accessToken))
                    .refreshTokenExpiration(jwtService.parseExpiration(refreshToken))
                    .build();
        }

}
