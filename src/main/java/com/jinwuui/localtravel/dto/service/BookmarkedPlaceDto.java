package com.jinwuui.localtravel.dto.service;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkedPlaceDto {

    private Long placeId;

    private String name;

    private String description;

    private Long rating;

    private String country;

    private Boolean isBookmarked;
    
    private List<String> imageUrls;
    
}
