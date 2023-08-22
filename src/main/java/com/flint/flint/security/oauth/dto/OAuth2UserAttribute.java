package com.flint.flint.security.oauth.dto;

import com.flint.flint.member.domain.Member;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 정순원
 * @Since 2023-08-19
 */
@NoArgsConstructor
public abstract class OAuth2UserAttribute {

    private  Map<String, Object> attributes;

    public OAuth2UserAttribute(Map<String, Object> attributes) {
        this.attributes = new HashMap<>(attributes);
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getProviderId();
    public abstract String getEmail();

    public abstract String getName();

    public abstract String getGender();

    public abstract String getBirthday();

    public abstract Member toEntity();

    public abstract void UserAttributesByOAuthToken(OAuth2AccessToken OAuth2AccessToken);

}
