package com.jinwuui.howdoilook.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "아이디는 필수 입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 입니다.")
    private String nickname;

    private LocalDateTime createdAt;

    @Builder
    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.createdAt = LocalDateTime.now();
    }
}
