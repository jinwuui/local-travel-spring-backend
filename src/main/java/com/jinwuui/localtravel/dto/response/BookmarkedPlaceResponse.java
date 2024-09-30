package com.jinwuui.localtravel.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkedPlaceResponse {

    private Long placeId;

    private String name;

    private String description;

    private Long rating;

    private String country;

    private Boolean isBookmarked;

    private List<String> imageUrls;

}
