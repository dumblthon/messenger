package com.dumblthon.messenger.auth.model;

import java.util.Objects;

public class UserSecret {

    private final Long userId;
    private final String deviceId;
    private final String secret;

    public UserSecret(Long userId, String deviceId, String secret) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.secret = secret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSecret that = (UserSecret) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(deviceId, that.deviceId) &&
                Objects.equals(secret, that.secret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, deviceId, secret);
    }

    @Override
    public String toString() {
        return "UserSecret{" +
                "userId=" + userId +
                ", deviceId='" + deviceId + '\'' +
                ", secret='" + secret + '\'' +
                '}';
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
