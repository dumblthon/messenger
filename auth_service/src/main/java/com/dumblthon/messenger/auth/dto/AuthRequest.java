package com.dumblthon.messenger.auth.dto;

import javax.validation.constraints.Pattern;

public class AuthRequest {

    // Код страны (от 1 до 3) и 10 цифр телефона
    // Код Бермудских островов +1-441 (и таких еще много)
    // Можно добавить поле "Страна" и подставлять код
    @Pattern(regexp = "[0-9]{1,3}[0-9]{10}", message = "Incorrect phone number")
    private String phoneNumber;

    private String deviceId;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
