package com.jinwuui.localtravel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jinwuui.localtravel.domain.Bookmark;
import com.jinwuui.localtravel.domain.Category;
import com.jinwuui.localtravel.domain.Image;
import com.jinwuui.localtravel.domain.Place;
import com.jinwuui.localtravel.domain.User;
import com.jinwuui.localtravel.dto.service.PlaceDetailDto;
import com.jinwuui.localtravel.dto.service.PlaceSimpleDto;
import com.jinwuui.localtravel.exception.PlaceNotFoundException;
import com.jinwuui.localtravel.exception.UserNotFoundException;
import com.jinwuui.localtravel.dto.service.BookmarkedPlaceDto;
import com.jinwuui.localtravel.repository.BookmarkRepository;
import com.jinwuui.localtravel.repository.CategoryRepository;
import com.jinwuui.localtravel.repository.PlaceCategoryRepository;
import com.jinwuui.localtravel.repository.PlaceRepository;
import com.jinwuui.localtravel.repository.UserRepository;

import jakarta.persistence.EntityManager;
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

    @Autowired
    private PlaceCategoryRepository placeCategoryRepository;

    @Autowired
    private EntityManager entityManager;

    @AfterEach
    void clearAll() {
        placeCategoryRepository.deleteAll();
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
        assertEquals(false, placeSimpleDto.getIsBookmarked());
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
        // place1의 isBookmarked이 true인지 확인
        assertTrue(result1.stream()
                .filter(dto -> dto.getPlaceId().equals(place1.getId()))
                .findFirst()
                .orElseThrow()
                .getIsBookmarked());

        // place2의 isBookmarked이 false인지 확인
        assertFalse(result1.stream()
                .filter(dto -> dto.getPlaceId().equals(place2.getId()))
                .findFirst()
                .orElseThrow()
                .getIsBookmarked());
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

        // result1 - place1의 isBookmarked이 true인지 확인
        assertTrue(result1.stream()
                .filter(dto -> dto.getPlaceId().equals(place1.getId()))
                .findFirst()
                .orElseThrow()
                .getIsBookmarked());

        // result1 - place3의 isBookmarked이 false인지 확인
        assertFalse(result1.stream()
                .filter(dto -> dto.getPlaceId().equals(place3.getId()))
                .findFirst()
                .orElseThrow()
                .getIsBookmarked());

        // result2 - place1의 isBookmarked이 true인지 확인
        assertTrue(result2.stream()
                .filter(dto -> dto.getPlaceId().equals(place1.getId()))
                .findFirst()
                .orElseThrow()
                .getIsBookmarked());

        // result2 - place2의 isBookmarked이 false인지 확인
        assertFalse(result2.stream()
                .filter(dto -> dto.getPlaceId().equals(place2.getId()))
                .findFirst()
                .orElseThrow()
                .getIsBookmarked());
    }

    @Test
    @Transactional
    @DisplayName("장소 상세 조회 - 익명 사용자")
    void readPlaceDetailForAnonymous() {
        // given
        Category category = Category.builder()
                .name("테스트 카테고리")
                .build();
        categoryRepository.save(category);

        Place place = Place.builder()
                .name("테스트 장소")
                .description("테스트 설명")
                .lat(37.5665)
                .lng(126.9780)
                .rating(4L)
                .build();
        place.addCategory(category);
        placeRepository.save(place);

        // when
        PlaceDetailDto result = placeService.read(Optional.empty(), place.getId());

        // then
        assertEquals(place.getId(), result.getPlaceId());
        assertEquals(place.getName(), result.getName());
        assertEquals(place.getDescription(), result.getDescription());
        assertEquals(place.getLat(), result.getLat());
        assertEquals(place.getLng(), result.getLng());
        assertEquals(place.getRating(), result.getRating());
        assertEquals(1, result.getCategories().size());
        assertEquals(category.getName(), result.getCategories().get(0));
        assertFalse(result.getIsBookmarked());
    }

    @Test
    @Transactional
    @DisplayName("장소 상세 조회 - 회원 사용자 / 북마크된 장소")
    void readPlaceDetailForUser() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .nickname("테스트 사용자")
                .password("password")
                .build();
        userRepository.save(user);

        Category category = Category.builder()
                .name("테스트 카테고리")
                .build();
        categoryRepository.save(category);

        Place place = Place.builder()
                .name("테스트 장소")
                .description("테스트 설명")
                .lat(37.5665)
                .lng(126.9780)
                .rating(4L)
                .user(user)
                .build();
        place.addCategory(category);
        placeRepository.save(place);

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .place(place)
                .build();
        bookmarkRepository.save(bookmark);

        // when
        PlaceDetailDto result = placeService.read(Optional.of(user.getId()), place.getId());

        // then
        assertEquals(place.getId(), result.getPlaceId());
        assertEquals(place.getName(), result.getName());
        assertEquals(place.getDescription(), result.getDescription());
        assertEquals(place.getLat(), result.getLat());
        assertEquals(place.getLng(), result.getLng());
        assertEquals(place.getRating(), result.getRating());
        assertEquals(1, result.getCategories().size());
        assertEquals(category.getName(), result.getCategories().get(0));
        assertTrue(result.getIsBookmarked());
    }

    @Test
    @Transactional
    @DisplayName("장소 상세 조회 - 카테고리 없고 이미지 있는 경우")
    void readPlaceDetailWithoutCategoryAndWithImage() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .nickname("테스트 사용자")
                .password("password")
                .build();
        userRepository.save(user);

        Place place = Place.builder()
                .name("테스트 장소")
                .description("테스트 설명")
                .lat(37.5665)
                .lng(126.9780)
                .rating(4L)
                .user(user)
                .build();

        Image image1 = Image.builder()
                .url("http://example.com/image1.jpg")
                .place(place)
                .build();
        Image image2 = Image.builder()
                .url("http://example.com/image2.jpg")
                .place(place)
                .build();

        place.addImage(image1);
        place.addImage(image2);

        placeRepository.save(place);

        entityManager.flush();
        entityManager.clear();  

        // when
        PlaceDetailDto result = placeService.read(Optional.of(user.getId()), place.getId());

        // then
        assertEquals(place.getId(), result.getPlaceId());
        assertEquals(place.getName(), result.getName());
        assertEquals(place.getDescription(), result.getDescription());
        assertEquals(place.getLat(), result.getLat());
        assertEquals(place.getLng(), result.getLng());
        assertEquals(place.getRating(), result.getRating());
        assertTrue(result.getCategories().isEmpty());
        assertFalse(result.getIsBookmarked());
        assertEquals(2, result.getImageUrls().size());
        assertTrue(result.getImageUrls().contains("http://example.com/image1.jpg"));
        assertTrue(result.getImageUrls().contains("http://example.com/image2.jpg"));
    }

    @Test
    @Transactional
    @DisplayName("북마크 장소 조회")
    void readBookmarks() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .nickname("테스트유저")
                .password("password")
                .build();
        userRepository.save(user);

        Place place1 = Place.builder()
                .name("북마크된 장소1")
                .description("설명1")
                .lat(37.5665)
                .lng(126.9780)
                .rating(4L)
                .user(user)
                .build();

        Place place2 = Place.builder()
                .name("북마크된 장소2")
                .description("설명2")
                .lat(37.5665)
                .lng(126.9780)
                .rating(5L)
                .user(user)
                .build();

        placeRepository.saveAll(List.of(place1, place2));

        Image image1 = Image.builder()
                .url("http://example.com/image1.jpg")
                .place(place1)
                .build();
        Image image2 = Image.builder()
                .url("http://example.com/image2.jpg")
                .place(place2)
                .build();

        place1.addImage(image1);
        place2.addImage(image2);

        Bookmark bookmark1 = Bookmark.builder()
                .user(user)
                .place(place1)
                .build();
        Bookmark bookmark2 = Bookmark.builder()
                .user(user)
                .place(place2)
                .build();

        bookmarkRepository.saveAll(List.of(bookmark1, bookmark2));

        entityManager.flush();
        entityManager.clear();

        // when
        List<BookmarkedPlaceDto> result = placeService.readBookmarks(user.getId());

        // then
        assertEquals(2, result.size());

        BookmarkedPlaceDto dto1 = result.get(0);
        assertEquals(place1.getId(), dto1.getPlaceId());
        assertEquals(place1.getName(), dto1.getName());
        assertEquals(place1.getDescription(), dto1.getDescription());
        assertEquals(place1.getRating(), dto1.getRating());
        assertTrue(dto1.getIsBookmarked());
        assertEquals(1, dto1.getImageUrls().size());
        assertTrue(dto1.getImageUrls().contains("http://example.com/image1.jpg"));

        BookmarkedPlaceDto dto2 = result.get(1);
        assertEquals(place2.getId(), dto2.getPlaceId());
        assertEquals(place2.getName(), dto2.getName());
        assertEquals(place2.getDescription(), dto2.getDescription());
        assertEquals(place2.getRating(), dto2.getRating());
        assertTrue(dto2.getIsBookmarked());
        assertEquals(1, dto2.getImageUrls().size());
        assertTrue(dto2.getImageUrls().contains("http://example.com/image2.jpg"));
    }

    @Test
    @Transactional
    @DisplayName("북마크된 장소와 북마크되지 않은 장소가 존재할 때 조회")
    void readBookmarksWithMixedBookmarkStatus() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .nickname("테스트 유저")
                .password("password")
                .build();
        userRepository.save(user);

        Place place1 = Place.builder()
                .name("북마크된 장소")
                .description("북마크된 장소 설명")
                .lat(37.5665)
                .lng(126.9780)
                .rating(4L)
                .build();
        Place place2 = Place.builder()
                .name("북마크되지 않은 장소")
                .description("북마크되지 않은 장소 설명")
                .lat(37.5667)
                .lng(126.9782)
                .rating(3L)
                .build();
        placeRepository.saveAll(List.of(place1, place2));

        Image image1 = Image.builder()
                .url("http://example.com/image1.jpg")
                .place(place1)
                .build();
        Image image2 = Image.builder()
                .url("http://example.com/image2.jpg")
                .place(place2)
                .build();
        place1.addImage(image1);
        place2.addImage(image2);

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .place(place1)
                .build();
        bookmarkRepository.save(bookmark);

        entityManager.flush();
        entityManager.clear();

        // when
        List<BookmarkedPlaceDto> result = placeService.readBookmarks(user.getId());

        // then
        assertEquals(1, result.size());

        BookmarkedPlaceDto dto = result.get(0);
        assertEquals(place1.getId(), dto.getPlaceId());
        assertEquals(place1.getName(), dto.getName());
        assertEquals(place1.getDescription(), dto.getDescription());
        assertEquals(place1.getRating(), dto.getRating());
        assertTrue(dto.getIsBookmarked());
        assertEquals(1, dto.getImageUrls().size());
        assertTrue(dto.getImageUrls().contains("http://example.com/image1.jpg"));
    }

    @Test
    @DisplayName("북마크 토글 테스트")
    void toggleBookmark() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .password("password")
                .nickname("테스트 유저")
                .build();
        userRepository.save(user);

        Place place = Place.builder()
                .name("테스트 장소")
                .description("테스트 장소 설명")
                .lat(37.5665)
                .lng(126.9780)
                .rating(4L)
                .build();
        placeRepository.save(place);

        // when & then
        // 북마크 추가
        boolean isBookmarked = placeService.toggleBookmark(user.getId(), place.getId());
        assertEquals(true, isBookmarked);
        assertTrue(bookmarkRepository.existsByUserIdAndPlaceId(user.getId(), place.getId()));

        // 북마크 제거
        isBookmarked = placeService.toggleBookmark(user.getId(), place.getId());
        assertEquals(false, isBookmarked);
        assertFalse(bookmarkRepository.existsByUserIdAndPlaceId(user.getId(), place.getId()));
    }

    @Test
    @DisplayName("존재하지 않는 유저 ID로 북마크 토글 시 예외 발생")
    void toggleBookmarkWithNonExistentUser() {
        // given
        Long nonExistentUserId = 9999L;
        Place place = Place.builder()
                .name("테스트 장소")
                .description("테스트 장소 설명")
                .lat(37.5665)
                .lng(126.9780)
                .rating(4L)
                .build();
        placeRepository.save(place);

        // when & then
        assertThrows(UserNotFoundException.class, () -> {
            placeService.toggleBookmark(nonExistentUserId, place.getId());
        });
    }

    @Test
    @DisplayName("존재하지 않는 장소 ID로 북마크 토글 시 예외 발생")
    void toggleBookmarkWithNonExistentPlace() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .password("password")
                .nickname("테스트 유저")
                .build();
        userRepository.save(user);
        
        Long nonExistentPlaceId = 9999L;

        // when & then
        assertThrows(PlaceNotFoundException.class, () -> {
            placeService.toggleBookmark(user.getId(), nonExistentPlaceId);
        });
    }

    @Test
    @DisplayName("userId가 null일 때 북마크 토글 시 예외 발생")
    void toggleBookmarkWithNullUserId() {
        // given
        Place place = Place.builder()
                .name("테스트 장소")
                .description("테스트 장소 설명")
                .lat(37.5665)
                .lng(126.9780)
                .rating(4L)
                .build();
        placeRepository.save(place);  

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
                placeService.toggleBookmark(null, place.getId());
        });
    }

    @Test
    @DisplayName("placeId가 null일 때 북마크 토글 시 예외 발생")
    void toggleBookmarkWithNullPlaceId() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .password("password")
                .nickname("테스트 유저")
                .build();
        userRepository.save(user);
        
        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
                placeService.toggleBookmark(user.getId(), null);
        });
    }

}
