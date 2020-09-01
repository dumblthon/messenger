package com.dumblthon.messenger.auth.model;

import java.util.Date;
import java.util.Objects;

public class UserOtp {

    private final Long userId;
    private final String deviceId;
    private final String code;
    private final Date sentAt;
    // private String senderId;

    public UserOtp(Long userId, String deviceId, String code, Date sentAt) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.code = code;
        this.sentAt = sentAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserOtp userOtp = (UserOtp) o;
        return Objects.equals(userId, userOtp.userId) &&
                Objects.equals(deviceId, userOtp.deviceId) &&
                Objects.equals(code, userOtp.code) &&
                Objects.equals(sentAt, userOtp.sentAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, deviceId, code, sentAt);
    }

    @Override
    public String toString() {
        return "UserOtp{" +
                "userId=" + userId +
                ", deviceId='" + deviceId + '\'' +
                ", code='" + code + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }

    public Long getUserId() {
        return userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getCode() {
        return code;
    }

    public Date getSentAt() {
        return sentAt;
    }
}
