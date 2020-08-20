package com.dumblthon.messenger.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    private final long userId;

    public UserNotFoundException(long userId) {
        super("Пользователь не найден");
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

}
