package com.dumblthon.messenger.auth.model;

import java.util.Date;

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
