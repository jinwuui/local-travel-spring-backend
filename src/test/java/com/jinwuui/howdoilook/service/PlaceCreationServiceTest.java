package com.jinwuui.howdoilook.service;

import com.jinwuui.howdoilook.domain.Place;
import com.jinwuui.howdoilook.domain.User;
import com.jinwuui.howdoilook.dto.service.PlaceDto;
import com.jinwuui.howdoilook.exception.UserNotFoundException;
import com.jinwuui.howdoilook.repository.PlaceRepository;
import com.jinwuui.howdoilook.repository.ImageRepository;
import com.jinwuui.howdoilook.repository.S3Repository;
import com.jinwuui.howdoilook.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PlaceCreationServiceTest {

    @Autowired
    private PlaceCreationService placeCreationService;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private S3Repository s3Repository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clean() {
        placeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("장소와 이미지 생성")
    void createPlace() {
        // given
        User user = User.builder()
                .email("jinwuui@gmail.com")
                .nickname("jinwuui")
                .password("1234")
                .build();
        userRepository.save(user);

        MockMultipartFile file1 = new MockMultipartFile("images", "image1.jpg", "image/jpeg", "image1 content".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.jpg", "image/jpeg", "image2 content".getBytes(StandardCharsets.UTF_8));
        PlaceDto placeDto = PlaceDto.builder()
                .content("임시 내용")
                .images(List.of(file1, file2))
                .build();

        // when
        Long placeId = placeCreationService.createPlaceWithImages(user.getId(), placeDto);

        // then
        assertEquals(1L, placeRepository.count());
        assertEquals(2L, imageRepository.count());
        Place place = placeRepository.findAll().get(0);
        assertEquals(placeId, place.getId());
        assertEquals(placeDto.getContent(), place.getContent());
    }

    @Test
    @DisplayName("장소와 이미지 생성 실패 - 유저 찾기 실패")
    void createPlaceFailUserNotFound() {
        // given
        User user = User.builder()
                .email("jinwuui@gmail.com")
                .nickname("jinwuui")
                .password("1234")
                .build();
        userRepository.save(user);

        MockMultipartFile file1 = new MockMultipartFile("images", "image1.jpg", "image/jpeg", "image1 content".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.jpg", "image/jpeg", "image2 content".getBytes(StandardCharsets.UTF_8));
        PlaceDto placeDto = PlaceDto.builder()
                .content("임시 내용")
                .images(List.of(file1, file2))
                .build();

        // expected
        assertThrows(UserNotFoundException.class,
                () -> placeCreationService.createPlaceWithImages(user.getId() + 1L, placeDto));
    }

    @Test
    @DisplayName("장소와 이미지 생성 실패 - 이미지가 아닌 파일")
    void createPlaceFailNotImageFile() {
        // given
        User user = User.builder()
                .email("jinwuui@gmail.com")
                .nickname("jinwuui")
                .password("1234")
                .build();
        userRepository.save(user);

        MockMultipartFile file1 = new MockMultipartFile("notImages", "notImages.txt", "text/plain", "notImages content".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.jpg", "image/jpeg", "image2 content".getBytes(StandardCharsets.UTF_8));
        PlaceDto placeDto = PlaceDto.builder()
                .content("임시 내용")
                .images(List.of(file1, file2))
                .build();

        // when
        placeCreationService.createPlaceWithImages(user.getId(), placeDto);

        // then
        assertEquals(1L, imageRepository.count());
    }
}