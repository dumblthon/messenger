package com.dumblthon.messenger.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class MissingValidationInfoException extends RuntimeException {

    private final long userId;
    private final String deviceId;

    public MissingValidationInfoException(long userId, String deviceId) {
        super("Недостаточно информации для валидации одноразового пароля");
        this.userId = userId;
        this.deviceId = deviceId;
    }

    public long getUserId() {
        return userId;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
