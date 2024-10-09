package com.jinwuui.localtravel.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class LocalTravelException extends RuntimeException {

    public final Map<String, String> validation = new HashMap<>();

    public LocalTravelException(String message) {
        super(message);
    }

    public LocalTravelException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
