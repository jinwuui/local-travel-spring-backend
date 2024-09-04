package com.jinwuui.howdoilook.dto.mapper;

import com.jinwuui.howdoilook.dto.request.SignUpRequest;
import com.jinwuui.howdoilook.dto.service.SignUpDto;

public class SignUpMapper {

    public static SignUpDto toSignUpDto(SignUpRequest signUpRequest) {
        return SignUpDto.builder()
                .username(signUpRequest.getUsername())
                .password(signUpRequest.getPassword())
                .nickname(signUpRequest.getNickname())
                .build();
    }
}
