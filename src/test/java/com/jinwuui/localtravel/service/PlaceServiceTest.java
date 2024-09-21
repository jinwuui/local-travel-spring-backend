package com.jinwuui.localtravel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jinwuui.localtravel.domain.Bookmark;
import com.jinwuui.localtravel.domain.Category;
import com.jinwuui.localtravel.domain.Place;
import com.jinwuui.localtravel.domain.User;
import com.jinwuui.localtravel.dto.service.PlaceSimpleDto;
import com.jinwuui.localtravel.repository.BookmarkRepository;
import com.jinwuui.localtravel.repository.CategoryRepository;
import com.jinwuui.localtravel.repository.PlaceRepository;
import com.jinwuui.localtravel.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class PlaceServiceTest {

    @Autowired
    private PlaceService placeService;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    void clearAll() {
        bookmarkRepository.deleteAll();
        categoryRepository.deleteAll();
        placeRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("익명 사용자 장소 조회")
    void readForAnonymous() {
        // given
        // 장소 데이터 생성
        Place place = Place.builder()
                .name("테스트 장소")
                .description("테스트 설명")
                .lat(37.5665)
                .lng(126.9780)
                .rating(4L)
                .build();

        // 카테고리 생성 및 장소에 추가
        Category category1 = Category.builder()
                .name("테스트 카테고리1")
                .build();
        Category category2 = Category.builder()
                .name("테스트 카테고리2")
                .build();
        categoryRepository.save(category1);
        categoryRepository.save(category2);

        place.addCategory(category1);
        place.addCategory(category2);
        placeRepository.save(place);

        // when
        List<PlaceSimpleDto> result = placeService.readForAnonymous();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        PlaceSimpleDto placeSimpleDto = result.get(0);
        assertEquals(place.getName(), placeSimpleDto.getName());
        assertEquals(place.getLat(), placeSimpleDto.getLat());
        assertEquals(place.getLng(), placeSimpleDto.getLng());
        assertEquals(List.of(category1.getName(), category2.getName()), placeSimpleDto.getCategories());
        assertEquals(false, placeSimpleDto.getIsFavorite());
    }

    @Test
    @Transactional
    @DisplayName("익명 사용자 카테고리로 장소 조회")
    void readForAnonymousByCategory() {
        // given
        Category category1 = Category.builder()
                .name("테스트 카테고리1")
                .build();
        Category category2 = Category.builder()
                .name("테스트 카테고리2")
                .build();

        categoryRepository.saveAll(List.of(category1, category2));

        Place place1 = Place.builder()
                .name("테스트 장소1")
                .description("테스트 설명1")
                .lat(37.5665)
                .lng(126.9780)
                .rating(4L)
                .build();

        Place place2 = Place.builder()
                .name("테스트 장소2")
                .description("테스트 설명2")
                .lat(37.5665)
                .lng(126.9780)
                .rating(4L)
                .build();

        place1.addCategory(category1);
        place1.addCategory(category2);
        place2.addCategory(category2);

        placeRepository.save(place1);
        placeRepository.save(place2);

        // when
        List<PlaceSimpleDto> result1 = placeService.readForAnonymousByCategory("테스트 카테고리1");
        List<PlaceSimpleDto> result2 = placeService.readForAnonymousByCategory("테스트 카테고리2");

        // then
        assertEquals(1, result1.size());
        assertEquals(2, result2.size());
    }

    @Test
    @Transactional
    @DisplayName("회원 사용자 장소 조회 - 북마크 검사")
    void readForUser() {
        // given
        User user = User.builder()
                .email("이메일@google.com")
                .nickname("닉네임")
                .password("비밀번호")
                .build();

        userRepository.save(user);

        Category category1 = Category.builder()
                .name("테스트 카테고리1")
                .build();
        Category category2 = Category.builder()
                .name("테스트 카테고리2")
                .build();

        categoryRepository.saveAll(List.of(category1, category2));

        Place place1 = Place.builder()
                .name("테스트 장소1")
                .description("테스트 설명1")
                .lat(37.5665)
                .lng(126.9780)
                .rating(4L)
                .user(user)
                .build();

        Place place2 = Place.builder()
                .name("테스트 장소2")
                .description("테스트 설명2")
                .lat(37.5665)
                .lng(126.9780)
                .rating(4L)
                .user(user)
                .build();

        place1.addCategory(category1);
        place1.addCategory(category2);
        place2.addCategory(category2);

        placeRepository.saveAll(List.of(place1, place2));

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .place(place1)
                .build();
        bookmarkRepository.saveAll(List.of(bookmark));

        // when
        List<PlaceSimpleDto> result1 = placeService.readForUser(user.getId());

        // then
        assertEquals(2, result1.size());
        // place1의 isFavorite이 true인지 확인
        assertTrue(result1.stream()
                .filter(dto -> dto.getPlaceId().equals(place1.getId()))
                .findFirst()
                .orElseThrow()
                .getIsFavorite());

        // place2의 isFavorite이 false인지 확인
        assertFalse(result1.stream()
                .filter(dto -> dto.getPlaceId().equals(place2.getId()))
                .findFirst()
                .orElseThrow()
                .getIsFavorite());
    }

    @Test
    @Transactional
    @DisplayName("회원 사용자 카테고리로 장소 조회 - 북마크 검사")
    void readForUserByCategory() {
        // given
        User user = User.builder()
                .email("이메일@google.com")
                .nickname("닉네임")
                .password("비밀번호")
                .build();

        userRepository.save(user);

        Category category1 = Category.builder()
                .name("테스트 카테고리1")
                .build();
        Category category2 = Category.builder()
                .name("테스트 카테고리2")
                .build();

        categoryRepository.saveAll(List.of(category1, category2));

        Place place1 = Place.builder()
                .name("테스트 장소1")
                .description("테스트 설명1")
                .lat(37.5665)
                .lng(126.9780)
                .rating(4L)
                .user(user)
                .build();

        Place place2 = Place.builder()
                .name("테스트 장소2")
                .description("테스트 설명2")
                .lat(37.5665)
                .lng(126.9780)
                .rating(4L)
                .user(user)
                .build();

        Place place3 = Place.builder()
                .name("테스트 장소2")
                .description("테스트 설명2")
                .lat(37.5665)
                .lng(126.9780)
                .rating(4L)
                .user(user)
                .build();

        place1.addCategory(category1);
        place1.addCategory(category2);
        place2.addCategory(category2);
        place3.addCategory(category1);

        placeRepository.saveAll(List.of(place1, place2, place3));

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .place(place1)
                .build();
        bookmarkRepository.saveAll(List.of(bookmark));

        // when
        List<PlaceSimpleDto> result1 = placeService.readForUserByCategory(user.getId(), "테스트 카테고리1");
        List<PlaceSimpleDto> result2 = placeService.readForUserByCategory(user.getId(), "테스트 카테고리2");

        // then
        assertEquals(2, result1.size());
        assertEquals(2, result2.size());

        // result1 - place1의 isFavorite이 true인지 확인
        assertTrue(result1.stream()
                .filter(dto -> dto.getPlaceId().equals(place1.getId()))
                .findFirst()
                .orElseThrow()
                .getIsFavorite());

        // result1 - place3의 isFavorite이 false인지 확인
        assertFalse(result1.stream()
                .filter(dto -> dto.getPlaceId().equals(place3.getId()))
                .findFirst()
                .orElseThrow()
                .getIsFavorite());

        // result2 - place1의 isFavorite이 true인지 확인
        assertTrue(result2.stream()
                .filter(dto -> dto.getPlaceId().equals(place1.getId()))
                .findFirst()
                .orElseThrow()
                .getIsFavorite());

        // result2 - place2의 isFavorite이 false인지 확인
        assertFalse(result2.stream()
                .filter(dto -> dto.getPlaceId().equals(place2.getId()))
                .findFirst()
                .orElseThrow()
                .getIsFavorite());
    }
}
