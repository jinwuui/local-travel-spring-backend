package com.jinwuui.howdoilook.dto.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
public class PlaceDto {

    private String name;

    private String description;

    private Double lat;

    private Double lng;

    private Long rating;

    private List<String> categories;

    private List<MultipartFile> images;

}
