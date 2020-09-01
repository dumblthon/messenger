package com.dumblthon.messenger.auth.exception;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        creatorVisibility = JsonAutoDetect.Visibility.NONE
)
public class MissingValidationInfoException extends RuntimeException {

    private final long userId;
    private final String deviceId;

    public MissingValidationInfoException(long userId, String deviceId) {
        super("Недостаточно информации для валидации одноразового пароля");
        this.userId = userId;
        this.deviceId = deviceId;
    }

    @JsonProperty
    public long getUserId() {
        return userId;
    }

    @JsonProperty
    public String getDeviceId() {
        return deviceId;
    }

    @JsonProperty
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
