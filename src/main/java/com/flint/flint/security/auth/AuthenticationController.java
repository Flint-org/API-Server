package com.flint.flint.security.auth;

import com.flint.flint.common.ResponseForm;
import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.domain.spec.Authority;
import com.flint.flint.member.domain.spec.Gender;
import com.flint.flint.member.repository.MemberRepository;
import com.flint.flint.security.auth.dto.AuthenticationResponse;
import com.flint.flint.security.auth.dto.ClaimsDTO;
import com.flint.flint.security.auth.dto.RegisterRequest;
import com.flint.flint.security.auth.jwt.JwtService;
import com.flint.flint.security.oauth.dto.OAuth2AccessToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 로그인, 회원가입, 토큰 재발급에 관한 API
 * @Author 정순원
 * @Since 2023-08-07
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    //테스트용
    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseForm<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) OAuth2AccessToken oAuth2AccessToken){
        AuthenticationResponse authenticationResponse = authenticationService.register(registerRequest, oAuth2AccessToken);
        return new ResponseForm<>(authenticationResponse);

    }

    @PostMapping("/login/{providerName}")
    public ResponseForm<AuthenticationResponse> login(@PathVariable String providerName, @RequestHeader(HttpHeaders.AUTHORIZATION) OAuth2AccessToken oAuth2AccessToken) {
        AuthenticationResponse authenticationResponse = authenticationService.login(providerName, oAuth2AccessToken);
        return new ResponseForm<>(authenticationResponse);
    }

//TODO
//    @PostMapping("/withdraw")
//    public void removeMember(@PathVariable String id) {
//        Optional<Member> member = memberRepository.findById(id);
//        memberRepository.deleteById(id);
//        authenticationService.deleteRedisToken(email);
//    }

    @PostMapping("/renew")
    public ResponseForm<AuthenticationResponse> newTokenByRefreshToken (@RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken) {
        AuthenticationResponse authenticationResponse = authenticationService.newTokenByRefreshToken(refreshToken);
        return new ResponseForm<>(authenticationResponse);
    }
    @GetMapping("/test")
    public String test() {
        Member member = Member.builder()
                .providerId("123")
                .authority(Authority.ANAUTHUSER)
                .birthday(LocalDate.parse("2000-11-13"))
                .email("jsw5913@naver.com")
                .gender(Gender.MALE)
                .providerName("naver")
                .build();
        memberRepository.save(member);
        boolean b = memberRepository.existsByProviderId(member.getProviderId());
        ClaimsDTO claimsDTO = ClaimsDTO.from(member);
        String accessToken = jwtService.generateAccessToken(claimsDTO);
        return accessToken;
    }
}
