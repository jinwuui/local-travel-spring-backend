package com.jinwuui.localtravel.dto.mapper;

import com.jinwuui.localtravel.dto.request.SignUpRequest;
import com.jinwuui.localtravel.dto.service.SignUpDto;

public class SignUpMapper {

    public static SignUpDto toSignUpDto(SignUpRequest signUpRequest) {
        return SignUpDto.builder()
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .nickname(signUpRequest.getNickname())
                .build();
    }
}
