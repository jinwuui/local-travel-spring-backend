package com.jinwuui.howdoilook.service;

import com.jinwuui.howdoilook.domain.User;
import com.jinwuui.howdoilook.dto.service.SignUpDto;
import com.jinwuui.howdoilook.exception.AlreadyExistsEmailException;
import com.jinwuui.howdoilook.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

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
        assertEquals("admin", user.getEmail());
        assertEquals("1234", user.getPassword());
        assertEquals("관리자", user.getNickname());
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
}