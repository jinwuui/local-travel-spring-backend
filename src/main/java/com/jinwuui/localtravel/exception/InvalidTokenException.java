package com.jinwuui.localtravel.exception;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

public class InvalidTokenException extends LocalTravelException {

    private static final String MESSAGE = "[인증 오류] 401 잘못된 토큰입니다.";

    public InvalidTokenException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return SC_UNAUTHORIZED;
    }
}
