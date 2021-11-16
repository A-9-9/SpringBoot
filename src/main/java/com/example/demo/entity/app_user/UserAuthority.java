package com.example.demo.entity.app_user;

import java.util.Arrays;

public enum UserAuthority {
    ADMIN, NORMAL;

    public UserAuthority fromString(String key) {
        return Arrays.stream(values()).filter(v -> v.name().equalsIgnoreCase(key)).findFirst().orElse(null);
    }
}
