package com.flint.flint.custom_member;

import com.flint.flint.member.domain.main.Member;
import com.flint.flint.member.repository.MemberRepository;
import com.flint.flint.member.spec.Authority;
import com.flint.flint.security.auth.dto.AuthorityMemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collection;
import java.util.Set;

public class WithMockCustomMemberSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomMember> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomMember annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Collection<? extends GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority(annotation.role()));

        AuthorityMemberDTO authMember = AuthorityMemberDTO.builder()
                .id(1L)
                .email("test@test.com")
                .providerId("kakao")
                .providerId("test")
                .build();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(authMember, "", authorities));
        return context;
    }
}
