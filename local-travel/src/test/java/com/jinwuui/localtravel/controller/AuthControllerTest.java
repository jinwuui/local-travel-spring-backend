package com.jinwuui.localtravel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinwuui.localtravel.dto.request.SignUpRequest;
import com.jinwuui.localtravel.repository.UserRepository;
import com.jinwuui.localtravel.util.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    void signUp() throws Exception {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email("jinwuui@gmail.com")
                .password("1234")
                .nickname("관리자")
                .build();

        // expected
        mockMvc.perform(post("/api/v1/auth/signup")
                .content(objectMapper.writeValueAsString(signUpRequest))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    // TODO: 회원가입 이메일 형식 테스트
    // TODO: 회원가입 닉네임 형식 테스트
    // TODO: 회원가입 중복 테스트

    @Test
    @DisplayName("토큰 재발행")
    void refreshToken() throws Exception {
        // given
        String refreshToken = jwtUtil.generateRefreshToken("jinwuui@gmail.com");

        // expected
        mockMvc.perform(post("/api/v1/auth/refresh")
                .header("Authorization", "Bearer " + refreshToken)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("토큰 재발행 실패 - 빈 토큰")
    void refreshTokenFailEmptyToken() throws Exception {
        // given & expected
        mockMvc.perform(post("/api/v1/auth/refresh")
                .header("Authorization", "Bearer ")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("토큰 재발행 실패 - 이상한 토큰")
    void refreshTokenFailInvalidToken() throws Exception {
        // given & expected
        mockMvc.perform(post("/api/v1/auth/refresh")
                .header("Authorization", "Bearer 123456")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("토큰 재발행 실패 - 이상한 토큰 2")
    void refreshTokenFailInvalidToken2() throws Exception {
        // given & expected
        mockMvc.perform(post("/api/v1/auth/refresh")
                .header("Authorization", "Hello World!")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}
