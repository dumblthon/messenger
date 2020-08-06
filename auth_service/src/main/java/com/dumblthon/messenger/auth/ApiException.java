package com.dumblthon.messenger.auth;

import org.springframework.http.HttpStatus;

public class ApiException {

    private HttpStatus status;
    private String error;
    private String message;

    public ApiException(HttpStatus status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

}
