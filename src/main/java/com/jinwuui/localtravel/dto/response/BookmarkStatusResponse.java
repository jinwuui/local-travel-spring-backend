package com.jinwuui.localtravel.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkStatusResponse {

    private Long userId;

    private Long placeId;

    private Boolean isBookmarked;
    
}
