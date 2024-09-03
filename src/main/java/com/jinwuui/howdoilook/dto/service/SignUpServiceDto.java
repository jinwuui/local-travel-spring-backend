package com.jinwuui.howdoilook.dto.service;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpServiceDto {

    private String username;

    private String password;

    private String nickname;

}
