package com.jinwuui.localtravel.service;

import com.jinwuui.localtravel.domain.Place;
import com.jinwuui.localtravel.domain.User;
import com.jinwuui.localtravel.dto.service.PlaceDto;
import com.jinwuui.localtravel.exception.UserNotFoundException;
import com.jinwuui.localtravel.repository.*;
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
    private CategoryRepository categoryRepository;

    @Autowired
    private PlaceCategoryRepository placeCategoryRepository;

    @Autowired
    private S3Repository s3Repository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clean() {
        placeCategoryRepository.deleteAll();
        categoryRepository.deleteAll();
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

        MockMultipartFile file1 = new MockMultipartFile("images", "image1.jpg", "image/jpeg",
                "image1 content".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.jpg", "image/jpeg",
                "image2 content".getBytes(StandardCharsets.UTF_8));
        PlaceDto placeDto = PlaceDto.builder()
                .lat(37.5665)
                .lng(126.978)
                .name("좋은 여행지")
                .description("정말 좋아요")
                .rating(3L)
                .categories(List.of("모험", "축제", "음식"))
                .images(List.of(file1, file2))
                .build();

        // when
        Long placeId = placeCreationService.createPlaceWithImages(user.getId(), placeDto);

        // then
        assertEquals(3L, categoryRepository.count());
        assertEquals(1L, placeRepository.count());
        assertEquals(2L, imageRepository.count());
        Place place = placeRepository.findAll().get(0);
        assertEquals(placeId, place.getId());
        assertEquals(placeDto.getName(), place.getName());
        assertEquals(placeDto.getDescription(), place.getDescription());
        assertEquals(placeDto.getLat(), place.getLat());
        assertEquals(placeDto.getLng(), place.getLng());
        assertEquals(placeDto.getRating(), place.getRating());
        assertNotNull(place.getCountry());
    }

    @Test
    @DisplayName("장소와 이미지 생성 - 카테고리 없음")
    void createPlaceNoCategories() {
        // given
        User user = User.builder()
                .email("jinwuui@gmail.com")
                .nickname("jinwuui")
                .password("1234")
                .build();
        userRepository.save(user);

        MockMultipartFile file1 = new MockMultipartFile("images", "image1.jpg", "image/jpeg",
                "image1 content".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.jpg", "image/jpeg",
                "image2 content".getBytes(StandardCharsets.UTF_8));
        PlaceDto placeDto = PlaceDto.builder()
                .lat(37.5665)
                .lng(126.978)
                .name("좋은 여행지")
                .description("정말 좋아요")
                .rating(3L)
                .categories(List.of())
                .images(List.of(file1, file2))
                .build();

        // when
        Long placeId = placeCreationService.createPlaceWithImages(user.getId(), placeDto);

        // then
        assertEquals(0L, categoryRepository.count());
        assertEquals(1L, placeRepository.count());
        assertEquals(2L, imageRepository.count());
        Place place = placeRepository.findAll().get(0);
        assertEquals(placeId, place.getId());
        assertEquals(placeDto.getName(), place.getName());
        assertEquals(placeDto.getDescription(), place.getDescription());
        assertEquals(placeDto.getLat(), place.getLat());
        assertEquals(placeDto.getLng(), place.getLng());
        assertEquals(placeDto.getRating(), place.getRating());
        assertNotNull(place.getCountry());
    }

    @Test
    @DisplayName("장소와 이미지 생성 - 이미지 없음")
    void createPlaceNoImages() {
        // given
        User user = User.builder()
                .email("jinwuui@gmail.com")
                .nickname("jinwuui")
                .password("1234")
                .build();
        userRepository.save(user);

        PlaceDto placeDto = PlaceDto.builder()
                .lat(37.5665)
                .lng(126.978)
                .name("좋은 여행지")
                .description("정말 좋아요")
                .rating(3L)
                .categories(List.of("모험", "축제", "음식"))
                .images(List.of())
                .build();

        // when
        Long placeId = placeCreationService.createPlaceWithImages(user.getId(), placeDto);

        // then
        assertEquals(3L, categoryRepository.count());
        assertEquals(1L, placeRepository.count());
        assertEquals(0L, imageRepository.count());
        Place place = placeRepository.findAll().get(0);
        assertEquals(placeId, place.getId());
        assertEquals(placeDto.getName(), place.getName());
        assertEquals(placeDto.getDescription(), place.getDescription());
        assertEquals(placeDto.getLat(), place.getLat());
        assertEquals(placeDto.getLng(), place.getLng());
        assertEquals(placeDto.getRating(), place.getRating());
        assertNotNull(place.getCountry());
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

        MockMultipartFile file1 = new MockMultipartFile("images", "image1.jpg", "image/jpeg",
                "image1 content".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.jpg", "image/jpeg",
                "image2 content".getBytes(StandardCharsets.UTF_8));
        PlaceDto placeDto = PlaceDto.builder()
                .lat(37.5665)
                .lng(126.978)
                .name("좋은 여행지")
                .description("정말 좋아요")
                .rating(3L)
                .categories(List.of("모험", "축제", "음식"))
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

        MockMultipartFile file1 = new MockMultipartFile("notImages", "notImages.txt", "text/plain",
                "notImages content".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.jpg", "image/jpeg",
                "image2 content".getBytes(StandardCharsets.UTF_8));
        PlaceDto placeDto = PlaceDto.builder()
                .lat(37.5665)
                .lng(126.978)
                .name("좋은 여행지")
                .description("정말 좋아요")
                .rating(3L)
                .categories(List.of("모험", "축제", "음식"))
                .images(List.of(file1, file2))
                .build();

        // when
        placeCreationService.createPlaceWithImages(user.getId(), placeDto);

        // then
        assertEquals(1L, imageRepository.count());
    }
}
