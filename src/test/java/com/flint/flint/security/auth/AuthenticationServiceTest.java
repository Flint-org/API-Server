package com.flint.flint.security.auth;

import com.flint.flint.member.repository.MemberRepository;
import com.flint.flint.member.repository.PolicyRepository;
import com.flint.flint.redis.RedisUtil;
import com.flint.flint.security.auth.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {JwtService.class, MemberRepository.class, PolicyRepository.class, RedisUtil.class, AuthenticationService.class})
 class AuthenticationServiceTest {

    @MockBean
    private  JwtService jwtService;
    @MockBean
    private  MemberRepository memberRepository;
    @MockBean
    private  PolicyRepository policyRepository;
    @MockBean
    private  RedisUtil redisUtil;

    @Autowired
    private AuthenticationService authenticationService;

    @Test
    void register() {
        //given
    }

    @Test
    void login() {
    }

    @Test
    void newTokenByRefreshToken() {
    }
}