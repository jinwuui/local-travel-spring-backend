package com.jinwuui.localtravel.controller;

import com.jinwuui.localtravel.dto.request.RefreshTokenRequest;
import com.jinwuui.localtravel.dto.request.SignUpRequest;
import com.jinwuui.localtravel.dto.response.TokenResponse;
import com.jinwuui.localtravel.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.jinwuui.localtravel.dto.mapper.SignUpMapper.*;
import static com.jinwuui.localtravel.dto.mapper.TokenMapper.*;

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
                authService.generateTokens(refreshTokenRequest.getRefreshToken()));
    }
}
