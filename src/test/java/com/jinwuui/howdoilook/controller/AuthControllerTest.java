package com.jinwuui.howdoilook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinwuui.howdoilook.dto.request.SignUpRequestDto;
import com.jinwuui.howdoilook.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 테스트")
    void signUp() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .username("admin")
                .password("1234")
                .nickname("관리자")
                .build();

        // expected
        mockMvc.perform(post("/api/v1/auth/signup")
                        .content(objectMapper.writeValueAsString(signUpRequestDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    // TODO: 회원가입 중복 테스트
}