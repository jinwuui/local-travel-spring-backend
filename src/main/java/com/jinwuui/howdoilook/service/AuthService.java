package com.jinwuui.howdoilook.service;

import com.jinwuui.howdoilook.domain.User;
import com.jinwuui.howdoilook.dto.service.SignUpServiceDto;
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

    public void signUp(SignUpServiceDto signUpServiceDto) {
        Optional<User> userOptional = userRepository.findByUsername(signUpServiceDto.getUsername());
        if (userOptional.isPresent()) {
            throw new AlreadyExistsUsernameException();
        }

        User user = User.builder()
                .username(signUpServiceDto.getUsername())
                .password(signUpServiceDto.getPassword())
                .nickname(signUpServiceDto.getNickname())
                .build();
        userRepository.save(user);
    }
}
