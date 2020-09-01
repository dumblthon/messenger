package com.dumblthon.messenger.auth;

import com.dumblthon.messenger.auth.exception.MissingValidationInfoException;
import com.dumblthon.messenger.auth.exception.RequestValidationException;
import com.dumblthon.messenger.auth.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public UserNotFoundException handleUserNotFoundException(HttpServletRequest req, Exception e) {
        return  (UserNotFoundException) e;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MissingValidationInfoException.class)
    public MissingValidationInfoException handleMissingValidationInfoException(HttpServletRequest req, Exception e) {
        return (MissingValidationInfoException) e;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RequestValidationException.class)
    public RequestValidationException handleRequestValidationException(HttpServletRequest req, Exception e) {
        return (RequestValidationException) e;
    }

}
