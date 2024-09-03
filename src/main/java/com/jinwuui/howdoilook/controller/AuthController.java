package com.jinwuui.howdoilook.controller;

import com.jinwuui.howdoilook.dto.request.SignUpRequestDto;
import com.jinwuui.howdoilook.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signUp(signUpRequestDto.toServiceDto());
    }
}
