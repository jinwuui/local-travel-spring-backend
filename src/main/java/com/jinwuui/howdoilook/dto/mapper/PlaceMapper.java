package com.jinwuui.howdoilook.dto.mapper;

import com.jinwuui.howdoilook.dto.request.PlaceCreateRequest;
import com.jinwuui.howdoilook.dto.service.PlaceDto;

public class PlaceMapper {

    public static PlaceDto toPlaceDto(PlaceCreateRequest request) {
        return PlaceDto.builder()
                .name(request.getName())
                .description(request.getDescription())
                .lat(request.getLat())
                .lng(request.getLng())
                .rating(request.getRating())
                .categories(request.getCategories())
                .images(request.getImages())
                .build();
    }
}
