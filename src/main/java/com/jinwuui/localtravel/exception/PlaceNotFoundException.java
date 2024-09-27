package com.jinwuui.localtravel.exception;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class PlaceNotFoundException extends LocalTravelException {

    private static final String MESSAGE = "존재하지 않는 장소입니다.";

    public PlaceNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return SC_BAD_REQUEST;
    }
}
