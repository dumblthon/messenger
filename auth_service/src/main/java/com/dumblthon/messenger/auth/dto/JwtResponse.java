package com.dumblthon.messenger.auth.dto;

public class JwtResponse {

    private final String token;
    // add refresh token

    public JwtResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
