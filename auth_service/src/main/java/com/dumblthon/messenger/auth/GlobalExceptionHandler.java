package com.dumblthon.messenger.auth;

import com.dumblthon.messenger.auth.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("unused")
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<?> userAlreadyExistsHandler(HttpServletRequest req, Exception e) {
        UserAlreadyExistsException exception = (UserAlreadyExistsException) e;
        return MutableResponse.error(HttpStatus.CONFLICT, e)
                .add("userId", exception.getUserId())
                .get();
    }

}
