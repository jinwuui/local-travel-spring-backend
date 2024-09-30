package com.jinwuui.localtravel.dto.mapper;

import java.util.List;

import com.jinwuui.localtravel.dto.request.PlaceCreateRequest;
import com.jinwuui.localtravel.dto.response.BookmarkedPlaceResponse;
import com.jinwuui.localtravel.dto.response.PagingResponse;
import com.jinwuui.localtravel.dto.response.PlaceDetailResponse;
import com.jinwuui.localtravel.dto.response.PlaceResponse;
import com.jinwuui.localtravel.dto.service.BookmarkedPlaceDto;
import com.jinwuui.localtravel.dto.service.PlaceDetailDto;
import com.jinwuui.localtravel.dto.service.PlaceDto;
import com.jinwuui.localtravel.dto.service.PlaceSimpleDto;

public class PlaceMapper {

    public static PlaceDto toPlaceDto(PlaceCreateRequest request) {
        return PlaceDto.builder()
                .name(request.getName())
                .description(request.getDescription())
                .lat(request.getLat())
                .lng(request.getLng())
                .rating(request.getRating())
                .categories(request.getCategories())
                .images(request.getImages())
                .build();
    }

    public static PagingResponse<PlaceResponse> toPagingPlaceResponse(List<PlaceSimpleDto> placeSimpleDtos) {
        List<PlaceResponse> responses = placeSimpleDtos.stream()
                .map((PlaceSimpleDto dto) -> PlaceResponse.builder()
                        .placeId(dto.getPlaceId())
                        .name(dto.getName())
                        .lat(dto.getLat())
                        .lng(dto.getLng())
                        .categories(dto.getCategories())
                        .isBookmarked(dto.getIsBookmarked())
                        .build())
                .collect(java.util.stream.Collectors.toList());

        return PagingResponse.<PlaceResponse>builder()
                .size(responses.size())
                .items(responses)
                .build();
    }

    public static PlaceDetailResponse toPlaceDetailResponse(PlaceDetailDto placeDetailDto) {
        return PlaceDetailResponse.builder()
                .placeId(placeDetailDto.getPlaceId())
                .name(placeDetailDto.getName())
                .description(placeDetailDto.getDescription())
                .lat(placeDetailDto.getLat())
                .lng(placeDetailDto.getLng())
                .rating(placeDetailDto.getRating())
                .isBookmarked(placeDetailDto.getIsBookmarked())
                .categories(placeDetailDto.getCategories())
                .imageUrls(placeDetailDto.getImageUrls())
                .build();
    }

    public static PagingResponse<BookmarkedPlaceResponse> toPagingBookmarkedPlaceResponse(
            List<BookmarkedPlaceDto> bookmarkedPlaceDtos) {
        List<BookmarkedPlaceResponse> responses = bookmarkedPlaceDtos.stream()
                .map(dto -> BookmarkedPlaceResponse.builder()
                        .placeId(dto.getPlaceId())
                        .name(dto.getName())
                        .description(dto.getDescription())
                        .rating(dto.getRating())
                        .country(dto.getCountry())
                        .isBookmarked(dto.getIsBookmarked())
                        .imageUrls(dto.getImageUrls())
                        .build())
                .collect(java.util.stream.Collectors.toList());

        return PagingResponse.<BookmarkedPlaceResponse>builder()
                .size(responses.size())
                .items(responses)
                .build();
    }
}
