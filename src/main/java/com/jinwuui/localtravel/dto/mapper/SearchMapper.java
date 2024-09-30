package com.jinwuui.localtravel.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.jinwuui.localtravel.dto.response.AutocompleteResponse;
import com.jinwuui.localtravel.dto.response.PagingResponse;
import com.jinwuui.localtravel.dto.service.AutocompleteDto;

public class SearchMapper {

    public static PagingResponse<AutocompleteResponse> toPagingAutocompleteResponse(List<AutocompleteDto> autocompleteDtos) {
        List<AutocompleteResponse> responses = autocompleteDtos.stream()
                .map(dto -> AutocompleteResponse.builder()
                        .placeId(dto.getPlaceId())
                        .name(dto.getName())
                        .description(dto.getDescription())
                        .country(dto.getCountry())
                        .build())
                .collect(Collectors.toList());

        return PagingResponse.<AutocompleteResponse>builder()
                .size(responses.size())
                .items(responses)
                .build();
    }
}
