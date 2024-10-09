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
        String email = extractEmailFromToken(refreshToken);
        validateToken(refreshToken, email);

        String newAccessToken = jwtUtil.generateAccessToken(email);
        String newRefreshToken = jwtUtil.generateRefreshToken(email);

        return TokenDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    private String extractEmailFromToken(String refreshToken) {
        try {
            return jwtUtil.extractEmail(refreshToken);
        } catch (Exception e) {
            throw new InvalidTokenException(e);
        }
    }

    private void validateToken(String refreshToken, String email) {
        if (jwtUtil.isTokenNotValid(refreshToken, email)) {
            throw new InvalidTokenException();
        }
    }
}
