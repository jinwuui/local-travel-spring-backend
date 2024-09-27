package com.jinwuui.localtravel.dto.service;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedbackDto {

    private String writer;

    private String content;
    
}
