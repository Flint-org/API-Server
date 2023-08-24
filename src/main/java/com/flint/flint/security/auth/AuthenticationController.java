package com.flint.flint.security.auth;

import com.flint.flint.common.ResponseForm;
import com.flint.flint.security.auth.dto.AuthenticationResponse;
import com.flint.flint.security.auth.dto.RegisterRequest;
import com.flint.flint.security.oauth.dto.OAuth2AccessToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/register")
    public ResponseForm<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) OAuth2AccessToken oAuth2AccessToken){
        AuthenticationResponse authenticationResponse = authenticationService.register(registerRequest, oAuth2AccessToken);
        return new ResponseForm<>(authenticationResponse);

    }

    @GetMapping("/login/{providerName}")
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
}
