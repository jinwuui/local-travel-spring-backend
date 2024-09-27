package com.jinwuui.localtravel.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceResponse {

    private Long placeId;

    private String name;

    private Double lat;

    private Double lng;

    private Boolean isFavorite;

    private List<String> categories;

}
