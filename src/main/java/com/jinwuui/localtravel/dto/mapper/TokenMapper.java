package com.jinwuui.localtravel.dto.mapper;

import com.jinwuui.localtravel.dto.response.TokenResponse;
import com.jinwuui.localtravel.dto.service.TokenDto;

public class TokenMapper {

    public static TokenResponse toTokenResponse(TokenDto tokenDto) {
        return TokenResponse.builder()
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .build();
    }
}
