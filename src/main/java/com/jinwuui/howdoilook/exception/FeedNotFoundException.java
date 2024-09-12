package com.jinwuui.howdoilook.exception;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

public class FeedNotFoundException extends HowDoILookException {

    private static final String MESSAGE = "존재하지 않는 피드입니다.";

    public FeedNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return SC_BAD_REQUEST;
    }
}
