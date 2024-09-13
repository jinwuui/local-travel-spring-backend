package com.jinwuui.howdoilook.service;

import com.jinwuui.howdoilook.domain.Category;
import com.jinwuui.howdoilook.domain.Place;
import com.jinwuui.howdoilook.domain.User;
import com.jinwuui.howdoilook.dto.service.PlaceDto;
import com.jinwuui.howdoilook.exception.UserNotFoundException;
import com.jinwuui.howdoilook.repository.PlaceRepository;
import com.jinwuui.howdoilook.repository.UserRepository;
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

    public Place create(Long userId, PlaceDto placeDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Place place = Place.builder()
                .name(placeDto.getName())
                .description(placeDto.getDescription())
                .lat(placeDto.getLat())
                .lng(placeDto.getLng())
                .rating(placeDto.getRating())
                .user(user)
                .build();

        for (String categoryName : placeDto.getCategories()) {
            Category category = categoryService.findOrCreate(categoryName);
            place.addCategory(category);
        }

        return placeRepository.save(place);
    }
}
