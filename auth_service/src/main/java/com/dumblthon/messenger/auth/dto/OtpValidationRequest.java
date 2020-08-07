package com.dumblthon.messenger.auth.dto;

public class OtpValidationRequest {

    private long userId;
    private String code;

    public OtpValidationRequest() { }

    public long getUserId() {
        return userId;
    }

    public String getCode() {
        return code;
    }

}
