package com.jinwuui.localtravel.dto.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PlaceDto {

    private String name;

    private String description;

    private Double lat;

    private Double lng;

    private Long rating;

    private List<String> categories;

    private List<MultipartFile> images;

    @Builder
    public PlaceDto(String name, String description, Double lat, Double lng, Long rating, List<String> categories, List<MultipartFile> images) {
        this.name = name;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.rating = rating;
        this.categories = categories != null ? categories : new ArrayList<>();
        this.images = images != null ? images : new ArrayList<>();
    }
}
