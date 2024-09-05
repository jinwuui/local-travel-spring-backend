package com.jinwuui.howdoilook.service;

import com.jinwuui.howdoilook.domain.User;
import com.jinwuui.howdoilook.dto.service.SignUpDto;
import com.jinwuui.howdoilook.dto.service.TokenDto;
import com.jinwuui.howdoilook.exception.AlreadyExistsEmailException;
import com.jinwuui.howdoilook.exception.InvalidTokenException;
import com.jinwuui.howdoilook.repository.UserRepository;
import com.jinwuui.howdoilook.util.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signupSuccess() {
        // given
        SignUpDto signUpDto = SignUpDto.builder()
                .email("admin")
                .password("1234")
                .nickname("관리자")
                .build();

        // when
        authService.signUp(signUpDto);

        // then
        assertEquals(1, userRepository.count());

        User user = userRepository.findAll().iterator().next();
        assertEquals(signUpDto.getEmail(), user.getEmail());
        assertTrue(passwordEncoder.matches(signUpDto.getPassword(), user.getPassword()));
        assertEquals(signUpDto.getNickname(), user.getNickname());
    }

    @Test
    @DisplayName("중복된 아이디로 회원가입")
    void signupDuplicatedEmail() {
        // given
        User user = User.builder()
                .email("admin")
                .password("1234")
                .nickname("관리자")
                .build();
        userRepository.save(user);

        SignUpDto signUpDto = SignUpDto.builder()
                .email("admin")
                .password("1234")
                .nickname("관리자")
                .build();

        // expected
        assertThrows(AlreadyExistsEmailException.class, () -> authService.signUp(signUpDto));
    }

    @Test
    @DisplayName("토큰 생성")
    void generateTokens() {
        // given
        String refreshToken = jwtUtil.generateRefreshToken("jinwuui@gmail.com");

        // when
        TokenDto tokenDto = authService.generateTokens(refreshToken);

        // then
        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());
    }

    @Test
    @DisplayName("토큰 생성 실패 - 이상한 토큰")
    void generateTokensFailInvalidRefreshToken() {
        // given
        String refreshToken = "이상한리프레시토큰";

        // expected
        assertThrows(InvalidTokenException.class, () -> authService.generateTokens(refreshToken));
    }

    @Test
    @DisplayName("토큰 생성 실패 - null 토큰")
    void generateTokensFailNullRefreshToken() {
        // given
        String refreshToken = null;

        // expected
        assertThrows(InvalidTokenException.class, () -> authService.generateTokens(refreshToken));
    }

    @Test
    @DisplayName("토큰 생성 실패 - 빈 토큰")
    void generateTokensFailEmptyRefreshToken() {
        // given
        String refreshToken = "";

        // expected
        assertThrows(InvalidTokenException.class, () -> authService.generateTokens(refreshToken));
    }
}