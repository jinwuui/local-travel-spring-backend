package com.jinwuui.howdoilook.exception;

import lombok.Getter;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@Getter
public class AlreadyExistsEmailException extends HowDoILookException {

    private static final String MESSAGE = "이미 가입된 아이디입니다.";

    public AlreadyExistsEmailException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return SC_BAD_REQUEST;
    }
}
