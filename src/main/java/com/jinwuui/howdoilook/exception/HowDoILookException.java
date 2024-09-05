package com.jinwuui.howdoilook.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class HowDoILookException extends RuntimeException {

    public final Map<String, String> validation = new HashMap<>();

    public HowDoILookException(String message) {
        super(message);
    }

    public HowDoILookException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
