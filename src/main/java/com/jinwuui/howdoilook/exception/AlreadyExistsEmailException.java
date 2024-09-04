package com.jinwuui.howdoilook.exception;

import lombok.Getter;

 @Getter
public class AlreadyExistsEmailException extends HowDoILookException {

    private static final String MESSAGE = "이미 가입된 아이디입니다.";

    public AlreadyExistsEmailException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
