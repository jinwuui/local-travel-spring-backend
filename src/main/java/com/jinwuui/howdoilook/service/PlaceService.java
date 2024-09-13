package com.jinwuui.howdoilook.service;

import com.jinwuui.howdoilook.domain.Place;
import com.jinwuui.howdoilook.domain.User;
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

    private final PlaceRepository placeRepository;

    private final UserRepository userRepository;

    public Place create(Long userId, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Place place = Place.builder()
                .content(content)
                .user(user)
                .build();
        return placeRepository.save(place);
    }
}
