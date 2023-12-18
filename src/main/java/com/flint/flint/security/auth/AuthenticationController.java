package com.flint.flint.security.auth;

import com.flint.flint.common.ResponseForm;
import com.flint.flint.security.auth.dto.response.AuthenticationResponse;
import com.flint.flint.security.auth.dto.request.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

/**
 * 로그인, 회원가입, 토큰 재발급에 관한 API
 *
 * @Author 정순원
 * @Since 2023-08-07
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    //테스트용

    @PostMapping("/register")
    public ResponseForm<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest registerRequest, HttpServletRequest oauth2TokenWithBearer) {
        AuthenticationResponse authenticationResponse = authenticationService.register(registerRequest, oauth2TokenWithBearer);
        return new ResponseForm<>(authenticationResponse);
    }

    @PostMapping("/login/{providerName}")
    public ResponseForm<AuthenticationResponse> loginByOauth2Provider(@PathVariable String providerName, HttpServletRequest oauth2TokenWithBearer) {
        AuthenticationResponse authenticationResponse = authenticationService.loginByOauth2Provider(providerName, oauth2TokenWithBearer);
        return new ResponseForm<>(authenticationResponse);
    }

    @PostMapping("/login/accesstoken")
    public ResponseForm loginByAccessToken(HttpServletRequest accessTokenWithBearer) {
        authenticationService.loginByAccessToken(accessTokenWithBearer);
        return new ResponseForm<>();
    }


    @PostMapping("/renew")
    public ResponseForm<AuthenticationResponse> newTokenByRefreshToken(HttpServletRequest refreshTokenWithBearer) {
        AuthenticationResponse authenticationResponse = authenticationService.newTokenByRefreshToken(refreshTokenWithBearer);
        return new ResponseForm<>(authenticationResponse);
    }
}