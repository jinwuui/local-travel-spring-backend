package com.jinwuui.howdoilook.dto.mapper;

import com.jinwuui.howdoilook.dto.response.TokenResponse;
import com.jinwuui.howdoilook.dto.service.TokenDto;

public class TokenMapper {

    public static TokenResponse toTokenResponse(TokenDto tokenDto) {
        return TokenResponse.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .build();
    }
}
