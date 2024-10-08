package com.jinwuui.localtravel.service;

import com.jinwuui.localtravel.domain.Place;
import com.jinwuui.localtravel.dto.service.PlaceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceCreationService {

    private final PlaceService placeService;

    private final ImageService imageService;

    @Transactional
    public Long createPlaceWithImages(Long userId, PlaceDto placeDto) {
        Place place = placeService.create(userId, placeDto);

        imageService.saveImages(place, placeDto.getImages());

        return place.getId();
    }
}
