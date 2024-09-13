package com.jinwuui.howdoilook.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenResponse {

    private Long userId;

    private String accessToken;

    private String refreshToken;

}
