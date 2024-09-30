package com.jinwuui.localtravel.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PlaceDetailResponse {

    private Long placeId;

    private String name;

    private String description;

    private Double lat;

    private Double lng;

    private Long rating;

    private Boolean isBookmarked;

    private List<String> categories;

    private List<String> imageUrls;

}
