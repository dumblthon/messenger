package com.dumblthon.messenger.auth.dto;

public class ValidationRequest {

    private long userId;
    private String deviceId;
    private String code;

    public long getUserId() {
        return userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getCode() {
        return code;
    }

}
