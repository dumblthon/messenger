package com.dumblthon.messenger.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MutableResponse {

    public static final String STATUS_FIELD = "status";
    public static final String ERROR_FIELD = "error";
    public static final String MESSAGE_FIELD = "message";

    public static MutableResponse create(HttpStatus status) {
        return new MutableResponse(status, new HashMap<>());
    }

    public static MutableResponse error(HttpStatus status, Exception e) {
        Map<String, Object> map = new HashMap<>();
        map.put(STATUS_FIELD, status);
        map.put(ERROR_FIELD, e.getClass().getSimpleName());
        map.put(MESSAGE_FIELD, e.getMessage());
        return new MutableResponse(status, map);
    }

    public static MutableResponse error(HttpStatus status, String error) {
        Map<String, Object> map = new HashMap<>();
        map.put(STATUS_FIELD, status);
        map.put(ERROR_FIELD, error);
        return new MutableResponse(status, map);
    }

    private final HttpStatus status;
    private final Map<String, Object> fields;

    public MutableResponse(HttpStatus status, Map<String, Object> fields) {
        this.status = status;
        this.fields = fields;
    }

    public MutableResponse add(String key, Object value) {
        fields.put(key, value);
        return this;
    }

    public ResponseEntity<Map<String, Object>> get() {
        Map<String, Object> map = fields == null ?
                Collections.emptyMap() : fields;
        return ResponseEntity.status(status).body(map);
    }

}
