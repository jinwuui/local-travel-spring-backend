package com.jinwuui.howdoilook.controller;

import com.jinwuui.howdoilook.dto.response.ErrorResponse;
import com.jinwuui.howdoilook.exception.HowDoILookException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ExceptionHandler(HowDoILookException.class)
    public ResponseEntity<ErrorResponse> hodologException(HowDoILookException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        return ResponseEntity.status(statusCode)
                .body(body);
    }
}
