package com.jinwuui.howdoilook.service;

import com.jinwuui.howdoilook.domain.Category;
import com.jinwuui.howdoilook.domain.Place;
import com.jinwuui.howdoilook.domain.User;
import com.jinwuui.howdoilook.dto.service.PlaceDto;
import com.jinwuui.howdoilook.exception.PlaceNotFoundException;
import com.jinwuui.howdoilook.exception.UserNotFoundException;
import com.jinwuui.howdoilook.repository.PlaceRepository;
import com.jinwuui.howdoilook.repository.UserRepository;
import com.jinwuui.howdoilook.util.GeoUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {

    private final CategoryService categoryService;

    private final PlaceRepository placeRepository;

    private final UserRepository userRepository;

    private final GeoUtil geoUtil;

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

        // TODO: chosungs, hanguls, alphabets 생성 - 비동기 가능
        
        // TODO: embedding 값 생성 - 비동기 가능

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
