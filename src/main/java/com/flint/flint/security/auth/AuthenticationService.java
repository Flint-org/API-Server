package com.flint.flint.security.auth;


import com.flint.flint.common.exception.FlintCustomException;
import com.flint.flint.common.spec.ResultCode;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.domain.main.Policy;
import com.flint.flint.member.repository.MemberRepository;
import com.flint.flint.member.repository.PolicyRepository;
import com.flint.flint.redis.RedisUtil;
import com.flint.flint.security.auth.dto.response.AuthenticationResponse;
import com.flint.flint.security.auth.dto.ClaimsDTO;
import com.flint.flint.security.auth.dto.request.RegisterRequest;
import com.flint.flint.security.jwt.JwtService;
import com.flint.flint.security.oauth.OAuth2UserAttribute;
import com.flint.flint.security.oauth.OAuth2UserAttributeFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * 로그인, 회원가입, 토큰 재발급에 관한 서비스
 *
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
    @Value("${jwt.refreshTokenExpiration}")
    private long refreshTokenExpiration;

    /**
     * 회원가입
     * member 저장, 수신동의 저장, 엑세스,리프레쉬토큰 생성, redis에 리프레쉬 토큰 저장
     */
    @Transactional
    public AuthenticationResponse register(RegisterRequest registerRequest, HttpServletRequest oauth2TokenWithBearer) {
        //카카오인지 네이버인지 선택
        OAuth2UserAttribute oAuth2UserAttribute = OAuth2UserAttributeFactory.of(registerRequest.getProviderName());
        String oauth2AccessToken = jwtService.parseTokenFrom(oauth2TokenWithBearer);
        //정보 추출
        oAuth2UserAttribute.setUserAttributesByOauthToken(oauth2AccessToken);
        checkRegistration(oAuth2UserAttribute.getProviderId());
        Member member = saveInformation(registerRequest, oAuth2UserAttribute);
        return generateToken(member);
    }

    /**
     * Oauth2Provider 토큰으로 로그인
     * 첫 로그인 혹은 엑세스,리프레쉬 토큰 없을 때
     */
    @Transactional
    public AuthenticationResponse loginByOauth2Provider(String providerName, HttpServletRequest oauth2TokenWithBearer) {
        OAuth2UserAttribute oAuth2UserAttribute = OAuth2UserAttributeFactory.of(providerName);
        String oauth2AccessToken = jwtService.parseTokenFrom(oauth2TokenWithBearer);
        //정보 추출
        oAuth2UserAttribute.setUserAttributesByOauthToken(oauth2AccessToken);
        String providerId = oAuth2UserAttribute.getProviderId();
        Member member = memberRepository.findByProviderId(providerId).orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, ResultCode.USER_NOT_JOINED));
        return generateToken(member);
    }

    /**
     * 엑세스 토큰으로 로그인
     */
    @Transactional
    public void loginByAccessToken(HttpServletRequest accessTokenWithBearer) {
        String accessToken = jwtService.parseTokenFrom(accessTokenWithBearer);
        jwtService.isTokenValid(accessToken);
    }


    /**
     * 리프레쉬 토큰으로 로그인
     */
    @Transactional
    public AuthenticationResponse newTokenByRefreshToken(HttpServletRequest refreshTokenWithBearer) {
        String refreshToken = jwtService.parseTokenFrom(refreshTokenWithBearer);
        String providerId = jwtService.parseProviderId(refreshToken);
        Member member = memberRepository.findByProviderId(providerId).orElseThrow(() -> new FlintCustomException(HttpStatus.NOT_FOUND, ResultCode.USER_NOT_FOUND));
        jwtService.isTokenValid(refreshToken);
        if (redisUtil.findByKey(providerId).toString().equals(refreshToken)) {
            return generateToken(member);
        }
        throw new FlintCustomException(HttpStatus.BAD_REQUEST, ResultCode.REFRESHTOKEN_OUTDATED);
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


    private void checkRegistration(String providerId) {
        if (memberRepository.existsByProviderId(providerId))
            throw new FlintCustomException(HttpStatus.BAD_REQUEST, ResultCode.USER_ALREADY_JOIN);
    }

    private Member saveInformation(RegisterRequest registerRequest, OAuth2UserAttribute oAuth2UserAttribute) {
        Member member = oAuth2UserAttribute.toEntity();
        Policy policy = Policy.builder().
                member(member).
                registerRequest(registerRequest).
                build();
        memberRepository.save(member);
        policyRepository.save(policy);
        return member;
    }
}