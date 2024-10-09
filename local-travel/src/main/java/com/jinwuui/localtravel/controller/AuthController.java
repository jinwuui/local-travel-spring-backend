package com.jinwuui.localtravel.controller;

import com.jinwuui.localtravel.dto.request.SignUpRequest;
import com.jinwuui.localtravel.dto.response.TokenResponse;
import com.jinwuui.localtravel.exception.InvalidTokenException;
import com.jinwuui.localtravel.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.jinwuui.localtravel.dto.mapper.SignUpMapper.*;
import static com.jinwuui.localtravel.dto.mapper.TokenMapper.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public TokenResponse refresh(@RequestHeader("Authorization") String authorizationHeader) {
        String refreshToken = extractTokenFromHeader(authorizationHeader);

        return toTokenResponse(
                authService.generateTokens(refreshToken));
    }

    private String extractTokenFromHeader(String authorizationHeader) {
        Pattern pattern = Pattern.compile("Bearer (.+)");
        Matcher matcher = pattern.matcher(authorizationHeader);

        if (!matcher.matches()) {
            throw new InvalidTokenException();
        }
        return matcher.group(1);
    }
}
