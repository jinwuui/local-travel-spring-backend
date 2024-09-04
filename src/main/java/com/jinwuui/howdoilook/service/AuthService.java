package com.jinwuui.howdoilook.service;

import com.jinwuui.howdoilook.domain.User;
import com.jinwuui.howdoilook.dto.service.SignUpDto;
import com.jinwuui.howdoilook.exception.AlreadyExistsUsernameException;
import com.jinwuui.howdoilook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public void signUp(SignUpDto signUpDto) {
        Optional<User> userOptional = userRepository.findByUsername(signUpDto.getUsername());
        if (userOptional.isPresent()) {
            throw new AlreadyExistsUsernameException();
        }

        User user = User.builder()
                .username(signUpDto.getUsername())
                .password(signUpDto.getPassword())
                .nickname(signUpDto.getNickname())
                .build();
        userRepository.save(user);
    }
}
