package com.jinwuui.howdoilook.exception;

import lombok.Getter;

 @Getter
public class AlreadyExistsUsernameException extends HowDoILookException {

    private static final String MESSAGE = "이미 가입된 아이디입니다.";

    public AlreadyExistsUsernameException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
