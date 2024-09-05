package com.jinwuui.howdoilook.config;

import com.jinwuui.howdoilook.domain.User;
import com.jinwuui.howdoilook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

@RequiredArgsConstructor
public class CustomMockSecurityContext implements WithSecurityContextFactory<CustomMockUser> {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public SecurityContext createSecurityContext(CustomMockUser annotation) {
        String encryptedPassword = passwordEncoder.encode(annotation.password());

        User user = User.builder()
                .email(annotation.email())
                .password(encryptedPassword)
                .nickname(annotation.nickname())
                .build();

        userRepository.save(user);

        UserPrincipal principal = new UserPrincipal(user);

        SimpleGrantedAuthority role = new SimpleGrantedAuthority("ROLE_User");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal,
                user.getPassword(),
                List.of(role));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationToken);

        return context;
    }
}
