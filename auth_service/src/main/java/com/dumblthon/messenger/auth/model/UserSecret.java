package com.dumblthon.messenger.auth.model;

public class UserSecret {

    private final Long userId;
    private final String deviceId;
    private final String secret;

    public UserSecret(Long userId, String deviceId, String secret) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.secret = secret;
    }

    public Long getUserId() {
        return userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getSecret() {
        return secret;
    }
}
