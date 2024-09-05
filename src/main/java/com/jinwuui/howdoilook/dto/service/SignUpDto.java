package com.jinwuui.howdoilook.dto.service;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpDto {

    private String email;

    private String password;

    private String nickname;

}
