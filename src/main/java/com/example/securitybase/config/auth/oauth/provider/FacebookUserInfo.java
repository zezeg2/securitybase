package com.example.securitybase.config.auth.oauth.provider;

import lombok.Builder;

import java.util.Map;

@Builder
public class FacebookUserInfo implements OAuth2UserInfo{
    private Map<String, Object> attributes; //oauth2user.getAttributes

    public FacebookUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "facebook";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
