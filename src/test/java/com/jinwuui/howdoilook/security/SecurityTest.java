package com.jinwuui.howdoilook.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinwuui.howdoilook.config.CustomMockUser;
import com.jinwuui.howdoilook.domain.User;
import com.jinwuui.howdoilook.dto.request.SignUpRequest;
import com.jinwuui.howdoilook.repository.UserRepository;
import com.jinwuui.howdoilook.util.JwtUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @CustomMockUser
    @DisplayName("로그인")
    void login() throws Exception {
        // given
        EmailPassword emailPassword = new EmailPassword();
        emailPassword.email = "jinwuui@gmail.com";
        emailPassword.password = "1234";

        // expected
        mockMvc.perform(post("/api/v1/auth/login")
                        .content(objectMapper.writeValueAsString(emailPassword))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andDo(print());
    }

    @Test
    @CustomMockUser
    @DisplayName("로그인 실패 - 틀린 아이디")
    void loginFailWrongId() throws Exception {
        // given
        EmailPassword emailPassword = new EmailPassword();
        emailPassword.email = "jjjjinwuui@gmail.com";
        emailPassword.password = "1234";

        // expected
        mockMvc.perform(post("/api/v1/auth/login")
                        .content(objectMapper.writeValueAsString(emailPassword))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @CustomMockUser
    @DisplayName("로그인 실패 - 틀린 비밀번호")
    void loginFailWrongPassword() throws Exception {
        // given
        EmailPassword emailPassword = new EmailPassword();
        emailPassword.email = "jinwuui@gmail.com";
        emailPassword.password = "12345";

        // expected
        mockMvc.perform(post("/api/v1/auth/login")
                        .content(objectMapper.writeValueAsString(emailPassword))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Getter
    private static class EmailPassword {
        private String email;
        private String password;
    }
}
