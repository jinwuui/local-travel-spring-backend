package com.jinwuui.localtravel.service;

import com.jinwuui.localtravel.domain.Bookmark;
import com.jinwuui.localtravel.domain.Category;
import com.jinwuui.localtravel.domain.Image;
import com.jinwuui.localtravel.domain.Place;
import com.jinwuui.localtravel.domain.User;
import com.jinwuui.localtravel.dto.service.BookmarkedPlaceDto;
import com.jinwuui.localtravel.dto.service.PlaceDetailDto;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
                    boolean isBookmarked = bookmarkRepository.existsByUserIdAndPlaceId(userId, place.getId());

                    List<String> categoryNames = place.getPlaceCategories().stream()
                            .map(placeCategory -> placeCategory.getCategory().getName())
                            .collect(Collectors.toList());

                    return PlaceSimpleDto.builder()
                            .placeId(place.getId())
                            .name(place.getName())
                            .lat(place.getLat())
                            .lng(place.getLng())
                            .categories(categoryNames)
                            .isBookmarked(isBookmarked)
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
                            .isBookmarked(false)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlaceSimpleDto> readForUserByCategory(Long userId, String category) {
        List<Place> places = placeRepository.findByCategoryName(category);

        return places.stream()
                .map((Place place) -> {
                    boolean isBookmarked = bookmarkRepository.existsByUserIdAndPlaceId(userId, place.getId());

                    List<String> categoryNames = place.getPlaceCategories().stream()
                            .map(placeCategory -> placeCategory.getCategory().getName())
                            .collect(Collectors.toList());

                    return PlaceSimpleDto.builder()
                            .placeId(place.getId())
                            .name(place.getName())
                            .lat(place.getLat())
                            .lng(place.getLng())
                            .categories(categoryNames)
                            .isBookmarked(isBookmarked)
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
                            .isBookmarked(false)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PlaceDetailDto read(Optional<Long> optionalUserId, Long placeId) {
        Place place = placeRepository.findByIdWithCategories(placeId)
            .orElseThrow(PlaceNotFoundException::new);

        List<String> categories = Optional.ofNullable(place.getPlaceCategories())
                .map(placeCategories -> placeCategories.stream()
                        .map(placeCategory -> placeCategory.getCategory().getName())
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        List<String> imageUrls = Optional.ofNullable(place.getImages())
                .map(images -> images.stream()
                        .map(Image::getUrl)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        boolean isBookmarked = optionalUserId.isPresent() &&  
                bookmarkRepository.existsByUserIdAndPlaceId(optionalUserId.get(), placeId);

        return PlaceDetailDto.builder()
                .placeId(place.getId())
                .name(place.getName())
                .description(place.getDescription())
                .lat(place.getLat())
                .lng(place.getLng())
                .rating(place.getRating())
                .isBookmarked(isBookmarked)
                .categories(categories)
                .imageUrls(imageUrls)
                .build();
    }
    
    @Transactional(readOnly = true)
    public List<BookmarkedPlaceDto> readBookmarks(Long userId) {
        if (userId == null) {
            throw new UserNotFoundException();
        }
        
        List<Place> bookmarkedPlaces = placeRepository.findBookmarkedPlacesWithImagesByUserId(userId);
        
        return bookmarkedPlaces.stream()
            .map((Place place) -> {
                List<String> imageUrls = Optional.ofNullable(place.getImages())
                    .map(images -> images.stream()
                        .map(Image::getUrl)
                        .collect(Collectors.toList()))
                    .orElse(Collections.emptyList());

                return BookmarkedPlaceDto.builder()
                    .placeId(place.getId())
                    .name(place.getName())
                    .description(place.getDescription())
                    .rating(place.getRating())
                    .country(place.getCountry())
                    .isBookmarked(true)
                    .imageUrls(imageUrls)
                    .build();
            })
            .collect(Collectors.toList());
    }

    @Transactional
    public boolean toggleBookmark(Long userId, Long placeId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId는 null일 수 없습니다.");
        }
        if (placeId == null) {
            throw new IllegalArgumentException("placeId는 null일 수 없습니다.");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Place place = placeRepository.findById(placeId)
                .orElseThrow(PlaceNotFoundException::new);

        boolean isBookmarked = bookmarkRepository.existsByUserIdAndPlaceId(userId, placeId);
        if (isBookmarked) {
            bookmarkRepository.deleteByUserAndPlace(user, place);
        } else {
            Bookmark bookmark = Bookmark.builder()
                    .user(user)
                    .place(place)
                    .build();
            bookmarkRepository.save(bookmark);
        }
        return !isBookmarked;
    }
}
