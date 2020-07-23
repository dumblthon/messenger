package com.dumblthon.messenger.auth.dto;

public class ValidationFailureResponse extends ValidationResponse {

    private final ValidationFailureReason reason;

    public ValidationFailureResponse(long userId, String deviceId,
                                     ValidationFailureReason reason) {
        super(userId, deviceId, false);
        this.reason = reason;
    }

    public ValidationFailureReason getReason() {
        return reason;
    }
}
