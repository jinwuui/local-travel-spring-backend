package com.jinwuui.localtravel.dto.service;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class AnnouncementDto {

    private Long id;

    private String version;

    private String content;

    private LocalDateTime createdAt;

}
