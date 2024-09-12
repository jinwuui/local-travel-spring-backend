package com.jinwuui.howdoilook.dto.mapper;

import com.jinwuui.howdoilook.dto.request.FeedCreateRequest;
import com.jinwuui.howdoilook.dto.request.SignUpRequest;
import com.jinwuui.howdoilook.dto.service.FeedDto;
import com.jinwuui.howdoilook.dto.service.SignUpDto;

public class FeedMapper {

    public static FeedDto toFeedDto(FeedCreateRequest feedCreateRequest) {
        return FeedDto.builder()
                .content(feedCreateRequest.getContent())
                .images(feedCreateRequest.getImages())
                .build();
    }
}
