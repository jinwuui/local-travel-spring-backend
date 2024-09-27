package com.jinwuui.localtravel.service;

import com.jinwuui.localtravel.domain.User;
import com.jinwuui.localtravel.dto.service.SignUpDto;
import com.jinwuui.localtravel.dto.service.TokenDto;
import com.jinwuui.localtravel.exception.AlreadyExistsEmailException;
import com.jinwuui.localtravel.exception.InvalidTokenException;
import com.jinwuui.localtravel.repository.UserRepository;
import com.jinwuui.localtravel.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public void signUp(SignUpDto signUpDto) {
        Optional<User> userOptional = userRepository.findByEmail(signUpDto.getEmail());
        if (userOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        String encryptedPassword = passwordEncoder.encode(signUpDto.getPassword());

        User user = User.builder()
                .email(signUpDto.getEmail())
                .password(encryptedPassword)
                .nickname(signUpDto.getNickname())
                .build();
        userRepository.save(user);
    }

    public TokenDto generateTokens(String refreshToken) {
        if (refreshToken == null) {
            throw new InvalidTokenException();
        }

        String email;
        try {
            email = jwtUtil.extractEmail(refreshToken);
            if (jwtUtil.isTokenNotValid(refreshToken, email)) {
                throw new InvalidTokenException();
            }
        } catch (Exception e) {
            log.error("토큰 재발행 오류 {}", e.getMessage(), e);
            throw new InvalidTokenException();
        }

        String newAccessToken = jwtUtil.generateAccessToken(email);
        String newRefreshToken = jwtUtil.generateRefreshToken(email);

        return TokenDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}
