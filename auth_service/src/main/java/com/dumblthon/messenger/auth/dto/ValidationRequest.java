package com.dumblthon.messenger.auth.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ValidationRequest {

    @NotNull(message = "Поле 'userId' не может быть пустым")
    private long userId;

    @NotEmpty(message = "Поле 'deviceId' не может быть пустым")
    private String deviceId;

    @NotEmpty(message = "Поле 'code' не может быть пустым")
    private String code;

    public ValidationRequest() {
    }

    public ValidationRequest(@NotNull long userId,
                             @NotNull String deviceId,
                             @NotNull String code) {
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
