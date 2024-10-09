package com.jinwuui.localtravel.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnnouncementResponse {

    private Long id;

    private String version;

    private String content;

    private LocalDateTime createdAt;

}
