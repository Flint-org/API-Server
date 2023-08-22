package com.flint.flint.security.oauth.dto;

import com.flint.flint.member.domain.Member;
import com.flint.flint.member.spec.Gender;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.Map;

/**
 * 카카오에서 받아오는 사용자 정보 담는 DTO
 * @Author 정순원
 * @Since 2023-08-19
 */
@NoArgsConstructor
public class KakaoOAuth2UserAttribute extends OAuth2UserAttribute {

    private static final String KAKAO_PROVIDER_ID = "kakao";
    private static final String KAKAO_ACCOUNT_KEY = "kakao_account";
    private static final String KAKAO_PROFILE_KEY = "profile";

    private Map<String, Object> kakaoAccount;
    private Map<String, Object> profile;

    public KakaoOAuth2UserAttribute(Map<String, Object> attributes) {
        super(attributes);
        this.kakaoAccount = (Map<String, Object>) attributes.get(KAKAO_ACCOUNT_KEY);
        this.profile = (Map<String, Object>) kakaoAccount.get(KAKAO_PROFILE_KEY);
    }
    @Override
    public Member toEntity() {
        return Member.builder()
                .providerName(KAKAO_PROVIDER_ID)
                .providerId(getProviderId())
                .email(getEmail())
                .name(getName())
                .gender(Gender.valueOf(getGender().toUpperCase())) //대소문자 구별하니 바꿔줘야 함
                .birthday(LocalDate.parse(getBirthday()))
                .build();
    }

    @Override
    public String getProviderId() {
        return kakaoAccount.get("id").toString();
    }

    @Override
    public String getEmail() {
        return kakaoAccount.get("email").toString();
    }

    @Override
    public String getName() {
        return kakaoAccount.get("name").toString();
    }

    @Override
    public String getGender() {
        return kakaoAccount.get("gender").toString();
    }

    @Override
    public String getBirthday() {
        return kakaoAccount.get("birthyear").toString();
    }

    @Override
    public void UserAttributesByOAuthToken(OAuth2AccessToken oauth2AccessToken) {


        KakaoOAuth2UserAttribute attribute = WebClient.create()
                .get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(oauth2AccessToken.getAccessToken()))
                .retrieve()
                .bodyToMono(KakaoOAuth2UserAttribute.class)
                .block();
        this.kakaoAccount = attribute.kakaoAccount;
        this.profile = attribute.profile;
    }
}
