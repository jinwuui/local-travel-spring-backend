package com.jinwuui.howdoilook.exception;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class UserNotFoundException extends HowDoILookException {

    private static final String MESSAGE = "존재하지 않는 사용자입니다.";

    public UserNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return SC_BAD_REQUEST;
    }
}
