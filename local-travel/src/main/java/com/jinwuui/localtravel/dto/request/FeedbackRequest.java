package com.jinwuui.localtravel.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequest {

    private String writer;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

}
