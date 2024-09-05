package com.jinwuui.howdoilook.controller;

import com.jinwuui.howdoilook.dto.mapper.SignUpMapper;
import com.jinwuui.howdoilook.dto.mapper.TokenMapper;
import com.jinwuui.howdoilook.dto.request.RefreshTokenRequest;
import com.jinwuui.howdoilook.dto.request.SignUpRequest;
import com.jinwuui.howdoilook.dto.response.TokenResponse;
import com.jinwuui.howdoilook.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.jinwuui.howdoilook.dto.mapper.SignUpMapper.*;
import static com.jinwuui.howdoilook.dto.mapper.TokenMapper.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody SignUpRequest signUpRequest) {
        authService.signUp(toSignUpDto(signUpRequest));
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return toTokenResponse(
                authService.generateTokens(refreshTokenRequest.getRefreshToken())
        );
    }
}
