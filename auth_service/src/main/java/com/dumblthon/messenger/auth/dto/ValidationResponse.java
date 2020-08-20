package com.dumblthon.messenger.auth.dto;

public abstract class ValidationResponse {

    private final long userId;
    private final String deviceId;
    private final boolean successful;

    public ValidationResponse(long userId,
                              String deviceId,
                              boolean successful) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.successful = successful;
    }

    public long getUserId() {
        return userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public boolean isSuccessful() {
        return successful;
    }
}
