package com.dumblthon.messenger.auth.otp.totp;

public class UserAlreadyExistsException extends RuntimeException {

    private long userId;

    public UserAlreadyExistsException(long userId) {
        this.userId = userId;
    }

    public UserAlreadyExistsException(String message, long userId) {
        super(message);
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }
}
