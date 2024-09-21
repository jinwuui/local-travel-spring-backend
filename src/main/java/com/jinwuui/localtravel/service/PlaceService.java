package com.jinwuui.localtravel.service;

import com.jinwuui.localtravel.domain.Category;
import com.jinwuui.localtravel.domain.Place;
import com.jinwuui.localtravel.domain.User;
import com.jinwuui.localtravel.dto.service.PlaceDto;
import com.jinwuui.localtravel.dto.service.PlaceSimpleDto;
import com.jinwuui.localtravel.exception.PlaceNotFoundException;
import com.jinwuui.localtravel.exception.UserNotFoundException;
import com.jinwuui.localtravel.repository.BookmarkRepository;
import com.jinwuui.localtravel.repository.PlaceRepository;
import com.jinwuui.localtravel.repository.UserRepository;
import com.jinwuui.localtravel.util.EmbeddingUtil;
import com.jinwuui.localtravel.util.GeoUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {

    private final CategoryService categoryService;

    private final PlaceRepository placeRepository;

    private final UserRepository userRepository;

    private final BookmarkRepository bookmarkRepository;

    private final GeoUtil geoUtil;

    private final EmbeddingUtil embeddingUtil;

    @Transactional
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

    @Transactional(readOnly = true)
    public List<PlaceSimpleDto> readForUser(Long userId) {
        List<Place> places = placeRepository.findAllWithCategories();

        return places.stream()
                .map((Place place) -> {
                    boolean isFavorite = bookmarkRepository.existsByUserIdAndPlaceId(userId, place.getId());

                    List<String> categoryNames = place.getPlaceCategories().stream()
                            .map(placeCategory -> placeCategory.getCategory().getName())
                            .collect(Collectors.toList());

                    return PlaceSimpleDto.builder()
                            .placeId(place.getId())
                            .name(place.getName())
                            .lat(place.getLat())
                            .lng(place.getLng())
                            .categories(categoryNames)
                            .isFavorite(isFavorite)
                            .build();
                })
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<PlaceSimpleDto> readForAnonymous() {
        List<Place> places = placeRepository.findAllWithCategories();

        return places.stream()
                .map((Place place) -> {
                    List<String> categoryNames = place.getPlaceCategories().stream()
                            .map(placeCategory -> placeCategory.getCategory().getName())
                            .collect(Collectors.toList());

                    return PlaceSimpleDto.builder()
                            .placeId(place.getId())
                            .name(place.getName())
                            .lat(place.getLat())
                            .lng(place.getLng())
                            .categories(categoryNames)
                            .isFavorite(false)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlaceSimpleDto> readForUserByCategory(Long userId, String category) {
        List<Place> places = placeRepository.findByCategoryName(category);

        return places.stream()
                .map((Place place) -> {
                    boolean isFavorite = bookmarkRepository.existsByUserIdAndPlaceId(userId, place.getId());

                    List<String> categoryNames = place.getPlaceCategories().stream()
                            .map(placeCategory -> placeCategory.getCategory().getName())
                            .collect(Collectors.toList());

                    return PlaceSimpleDto.builder()
                            .placeId(place.getId())
                            .name(place.getName())
                            .lat(place.getLat())
                            .lng(place.getLng())
                            .categories(categoryNames)
                            .isFavorite(isFavorite)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlaceSimpleDto> readForAnonymousByCategory(String category) {
        List<Place> places = placeRepository.findByCategoryName(category);

        return places.stream()
                .map((Place place) -> {
                    List<String> categoryNames = place.getPlaceCategories().stream()
                            .map(placeCategory -> placeCategory.getCategory().getName())
                            .collect(Collectors.toList());

                    return PlaceSimpleDto.builder()
                            .placeId(place.getId())
                            .name(place.getName())
                            .lat(place.getLat())
                            .lng(place.getLng())
                            .categories(categoryNames)
                            .isFavorite(false)
                            .build();
                })
                .collect(Collectors.toList());
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
