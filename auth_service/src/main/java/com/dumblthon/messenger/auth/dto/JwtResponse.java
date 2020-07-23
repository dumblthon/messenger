package com.dumblthon.messenger.auth.dto;

import com.dumblthon.messenger.auth.model.User;

public class JwtResponse {

    private final long userId;
    private final String token;

    public JwtResponse(User user, String token) {
        this.userId = user.getId();
        this.token = token;
    }

    public long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }
}
