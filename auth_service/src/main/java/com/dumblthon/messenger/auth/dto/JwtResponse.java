package com.dumblthon.messenger.auth.dto;

import com.dumblthon.messenger.auth.model.User;

public class JwtResponse {

    private User user;
    private String deviceId;
    private String token;

    public JwtResponse(User user, String deviceId, String token) {
        this.user = user;
        this.deviceId = deviceId;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getToken() {
        return token;
    }
}
