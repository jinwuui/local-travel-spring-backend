package com.jinwuui.howdoilook.dto.mapper;

import com.jinwuui.howdoilook.dto.request.PlaceCreateRequest;
import com.jinwuui.howdoilook.dto.service.PlaceDto;

public class PlaceMapper {

    public static PlaceDto toPlaceDto(PlaceCreateRequest placeCreateRequest) {
        return PlaceDto.builder()
                .content(placeCreateRequest.getContent())
                .images(placeCreateRequest.getImages())
                .build();
    }
}
