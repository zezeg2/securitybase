package com.example.securitybase.domain.user;

public enum Role {

    ROLE_MEMBER("ROLE_MEMBER"),
    ROLE_MANAGER("ROLE_MANAGER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String key;

    Role(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }


}
