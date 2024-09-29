package com.jinwuui.localtravel.dto.service;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AutocompleteDto {

    private Long placeId;

    private String name;

    private String description;

    private String country;

}
