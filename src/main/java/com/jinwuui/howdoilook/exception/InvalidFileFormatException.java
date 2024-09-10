package com.jinwuui.howdoilook.exception;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class InvalidFileFormatException extends HowDoILookException{

    private static final String MESSAGE = "잘못된 파일 형식입니다: ";

    public InvalidFileFormatException(String filename) {
        super(MESSAGE + filename);
    }

    @Override
    public int getStatusCode() {
        return SC_BAD_REQUEST;
    }
}
