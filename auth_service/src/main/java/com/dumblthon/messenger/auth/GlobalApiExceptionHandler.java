package com.dumblthon.messenger.auth;

import com.dumblthon.messenger.auth.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@RestControllerAdvice
public class GlobalApiExceptionHandler {

    public static Map<String, Object> mapResponseFrom(HttpStatus status, Exception e) {
        Map<String, Object> result = new HashMap<>();
        result.put("status", status);
        result.put("error", e.getClass().getSimpleName());
        result.put("message", e.getMessage());
        return result;
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public Map<String, Object> userAlreadyExistsHandler(HttpServletRequest req, Exception e) {
        Map<String, Object> response = mapResponseFrom(HttpStatus.CONFLICT, e);
        UserAlreadyExistsException exception = (UserAlreadyExistsException) e;
        response.put("userId", exception.getUserId());
        return response;
    }

}
