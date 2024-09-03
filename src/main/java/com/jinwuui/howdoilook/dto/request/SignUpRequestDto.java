package com.jinwuui.howdoilook.dto.request;

import com.jinwuui.howdoilook.dto.service.SignUpServiceDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequestDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    public SignUpServiceDto toServiceDto() {
        return SignUpServiceDto.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();
    }
}
