package com.jinwuui.localtravel.dto.service;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceDetailDto {

    private Long placeId;
    
    private String name;
    
    private String description;
    
    private Double lat;
    
    private Double lng;
    
    private Long rating;
    
    private Boolean isFavorite;
    
    private List<String> categories;
    
    private List<String> imageUrls;

}
