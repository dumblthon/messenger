package com.dumblthon.messenger.auth.dto;

public class ValidationSuccessResponse extends ValidationResponse {

    public ValidationSuccessResponse(long userId,
                                     String deviceId) {
        super(userId, deviceId, true);
    }

}
