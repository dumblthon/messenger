package com.dumblthon.messenger.auth.dto;

public class UserOtp {

    private long userId;
    private String code;

    public UserOtp() { }

    public long getUserId() {
        return userId;
    }

    public String getCode() {
        return code;
    }
}
