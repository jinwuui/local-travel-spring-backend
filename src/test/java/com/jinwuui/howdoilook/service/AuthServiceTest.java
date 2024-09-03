package com.jinwuui.howdoilook.service;

import com.jinwuui.howdoilook.domain.User;
import com.jinwuui.howdoilook.dto.request.SignUpRequestDto;
import com.jinwuui.howdoilook.dto.service.SignUpServiceDto;
import com.jinwuui.howdoilook.exception.AlreadyExistsUsernameException;
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

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signupSuccess() {
        // given
        SignUpServiceDto signUpServiceDto = SignUpServiceDto.builder()
                .username("admin")
                .password("1234")
                .nickname("관리자")
                .build();

        // when
        authService.signUp(signUpServiceDto);

        // then
        assertEquals(1, userRepository.count());

        User user = userRepository.findAll().iterator().next();
        assertEquals("admin", user.getUsername());
        assertEquals("1234", user.getPassword());
        assertEquals("관리자", user.getNickname());
    }

    @Test
    @DisplayName("중복된 아이디로 회원가입")
    void signupDuplicatedUsername() {
        // given
        User user = User.builder()
                .username("admin")
                .password("1234")
                .nickname("관리자")
                .build();
        userRepository.save(user);

        SignUpServiceDto signUpServiceDto = SignUpServiceDto.builder()
                .username("admin")
                .password("1234")
                .nickname("관리자")
                .build();

        // expected
        assertThrows(AlreadyExistsUsernameException.class, () -> authService.signUp(signUpServiceDto));
    }
}