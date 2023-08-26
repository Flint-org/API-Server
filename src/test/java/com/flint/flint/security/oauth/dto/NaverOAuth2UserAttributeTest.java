package com.flint.flint.security.oauth.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest(classes = {NaverOAuth2UserAttribute.class})

class NaverOAuth2UserAttributeTest {

    @Autowired
    private NaverOAuth2UserAttribute naverOAuth2UserAttribute;
    @Test
    void userAttributesByOAuthToken() {
        // Given
        OAuth2AccessToken token = new OAuth2AccessToken("");

        // When
        KakaoOAuth2UserAttribute kakaoOAuth2UserAttribute = new KakaoOAuth2UserAttribute();
        kakaoOAuth2UserAttribute.UserAttributesByOAuthToken(token);

        // Then
        System.out.println("Provider ID: " + kakaoOAuth2UserAttribute.getProviderId());
        System.out.println("Email: " + kakaoOAuth2UserAttribute.getEmail());
        System.out.println("Name: " + kakaoOAuth2UserAttribute.getName());
        System.out.println("Gender: " + kakaoOAuth2UserAttribute.getGender());
        System.out.println("Birthday: " + kakaoOAuth2UserAttribute.getBirthday());
        assertThat(kakaoOAuth2UserAttribute.getProviderId()).isNotNull();
        assertThat(kakaoOAuth2UserAttribute.getEmail()).isNotNull();
        assertThat(kakaoOAuth2UserAttribute.getName()).isNotNull();
        assertThat(kakaoOAuth2UserAttribute.getGender()).isNotNull();
        assertThat(kakaoOAuth2UserAttribute.getBirthday()).isNotNull();
    }
}