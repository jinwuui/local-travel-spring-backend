package com.jinwuui.localtravel.service;

import com.jinwuui.localtravel.domain.Category;
import com.jinwuui.localtravel.domain.Place;
import com.jinwuui.localtravel.domain.User;
import com.jinwuui.localtravel.dto.service.PlaceDto;
import com.jinwuui.localtravel.exception.PlaceNotFoundException;
import com.jinwuui.localtravel.exception.UserNotFoundException;
import com.jinwuui.localtravel.repository.PlaceRepository;
import com.jinwuui.localtravel.repository.UserRepository;
import com.jinwuui.localtravel.util.EmbeddingUtil;
import com.jinwuui.localtravel.util.GeoUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {

    private final CategoryService categoryService;

    private final PlaceRepository placeRepository;

    private final UserRepository userRepository;

    private final GeoUtil geoUtil;

    private final EmbeddingUtil embeddingUtil;

    public Place create(Long userId, PlaceDto placeDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

 
        // TODO: country 값 조회 - 동기 필수
        String country = null;
        try {
            country = geoUtil.getCountryName(placeDto.getLat(), placeDto.getLng());
        } catch (Exception e) {
            log.error("국가 정보 조회 중 오류 발생", e);
        }

        Place place = Place.builder()
                .name(placeDto.getName())
                .description(placeDto.getDescription())
                .lat(placeDto.getLat())
                .lng(placeDto.getLng())
                .rating(placeDto.getRating())
                .country(country)
                .user(user)
                .build();

        for (String categoryName : placeDto.getCategories()) {
            Category category = categoryService.findOrCreate(categoryName);
            place.addCategory(category);
        }

        List<Double> embedding = embeddingUtil.fetchEmbedding(place.getEmbeddingText());
        place.setEmbedding(embedding);

        return placeRepository.save(place);
    }

    public PlaceDto read(Long userId, Long placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(PlaceNotFoundException::new);

        // TODO: 카테고리 조회

        // TODO: 사진 조회

        // TODO: userId 존재하면 favorite 조회

        return PlaceDto.builder()
                .build();
    }
}
