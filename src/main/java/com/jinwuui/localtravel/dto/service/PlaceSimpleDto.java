package com.jinwuui.localtravel.dto.service;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PlaceSimpleDto {
    
    private Long placeId;

    private String name;

    private Double lat;

    private Double lng;

    private Boolean isFavorite;

    private List<String> categories;

}
