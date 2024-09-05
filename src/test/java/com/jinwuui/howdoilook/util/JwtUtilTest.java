package com.jinwuui.howdoilook.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("엑세스 토큰 생성")
    void generateAccessToken() {
        // given
        String email = "jinwuui@gmail.com";

        // when
        String accessToken = jwtUtil.generateAccessToken(email);

        // then
        assertTrue(jwtUtil.isTokenValid(accessToken, email));
    }

    @Test
    @DisplayName("리프레시 토큰 생성")
    void generateRefreshToken() {
        // given
        String email = "jinwuui@gmail.com";

        // when
        String accessToken = jwtUtil.generateRefreshToken(email);

        // then
        assertTrue(jwtUtil.isTokenValid(accessToken, email));
    }

    @Test
    @DisplayName("토큰 검증")
    void isTokenValid() {
        // given
        String email = "jinwuui@gmail.com";

        // when
        String accessToken = jwtUtil.generateAccessToken(email);

        // then
        assertTrue(jwtUtil.isTokenValid(accessToken, email));
        assertFalse(jwtUtil.isTokenNotValid(accessToken, email));
        assertTrue(jwtUtil.isTokenNotValid(accessToken, email + "abcde"));
        assertFalse(jwtUtil.isTokenValid(accessToken, email + "abcde"));
    }

    @Test
    @DisplayName("엑세스 토큰에서 이메일 추출")
    void extractEmailAccessToken() {
        // given
        String email = "jinwuui@gmail.com";
        String accessToken = jwtUtil.generateAccessToken(email);

        // when
        String extractedEmail = jwtUtil.extractEmail(accessToken);

        // then
        assertEquals(email, extractedEmail);
    }

    @Test
    @DisplayName("리프레시 토큰에서 이메일 추출")
    void extractEmailRefreshToken() {
        // given
        String email = "jinwuui@gmail.com";
        String accessToken = jwtUtil.generateRefreshToken(email);

        // when
        String extractedEmail = jwtUtil.extractEmail(accessToken);

        // then
        assertEquals(email, extractedEmail);
    }
}