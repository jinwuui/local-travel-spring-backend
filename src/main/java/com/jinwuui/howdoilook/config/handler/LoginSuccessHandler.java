package com.jinwuui.howdoilook.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinwuui.howdoilook.config.UserPrincipal;
import com.jinwuui.howdoilook.dto.response.TokenResponse;
import com.jinwuui.howdoilook.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        log.info("[인증 성공] 200 user={}", principal.getUsername());

        TokenResponse tokenResponse = TokenResponse.builder()
                .userId(principal.getUserId())
                .accessToken(jwtUtil.generateAccessToken(principal.getUsername()))
                .refreshToken(jwtUtil.generateRefreshToken(principal.getUsername()))
                .build();

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        response.setStatus(SC_OK);

        objectMapper.writeValue(response.getWriter(), tokenResponse);
    }
}
