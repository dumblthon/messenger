package com.dumblthon.messenger.auth.otp.totp;

public class UserOtpDto {

    private long userId;
    private String code;

    public UserOtpDto() { }

    public long getUserId() {
        return userId;
    }

    public String getCode() {
        return code;
    }
}
