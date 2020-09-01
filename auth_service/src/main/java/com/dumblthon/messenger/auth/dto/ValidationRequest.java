package com.dumblthon.messenger.auth.dto;

import javax.validation.constraints.NotNull;

public class ValidationRequest {

    @NotNull
    private long userId;

    @NotNull
    private String deviceId;

    @NotNull
    private String code;

    public ValidationRequest() {
    }

    public ValidationRequest(@NotNull long userId, @NotNull String deviceId, @NotNull String code) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.code = code;
    }

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
