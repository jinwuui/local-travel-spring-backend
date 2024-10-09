package com.jinwuui.localtravel.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AutocompleteResponse {

    private Long placeId;

    private String name;

    private String description;

    private String country;

}
