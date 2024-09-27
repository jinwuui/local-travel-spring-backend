package com.jinwuui.localtravel.controller;

import com.jinwuui.localtravel.config.CustomMockUser;
import com.jinwuui.localtravel.domain.Bookmark;
import com.jinwuui.localtravel.domain.Category;
import com.jinwuui.localtravel.domain.Image;
import com.jinwuui.localtravel.domain.Place;
import com.jinwuui.localtravel.domain.User;
import com.jinwuui.localtravel.repository.BookmarkRepository;
import com.jinwuui.localtravel.repository.CategoryRepository;
import com.jinwuui.localtravel.repository.PlaceCategoryRepository;
import com.jinwuui.localtravel.repository.PlaceRepository;
import com.jinwuui.localtravel.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsInAnyOrder;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class PlaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaceCategoryRepository placeCategoryRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @AfterEach
    void clean() {
        bookmarkRepository.deleteAll();
        placeCategoryRepository.deleteAll();
        categoryRepository.deleteAll();
        placeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @CustomMockUser
    @DisplayName("장소 생성")
    void postPlace() throws Exception {
        // given
        MockMultipartFile file1 = new MockMultipartFile("images", "image1.jpg", "image/jpeg", "image1 content".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.png", "image/png", "image2 content".getBytes(StandardCharsets.UTF_8));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/places")
                        .file(file1)
                        .file(file2)
                        .param("name", "Sample name")
                        .param("description", "Sample description")
                        .param("lat", "33.1111")
                        .param("lng", "128.1111")
                        .param("rating", "1")
                        .param("categories", "['모험', '음식']")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isCreated());
    }

    @Test
    @CustomMockUser
    @DisplayName("장소 생성 - 이미지 null")
    void postPlaceNullImages() throws Exception {
        // expected
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/places")
                        .param("name", "Sample name")
                        .param("description", "Sample description")
                        .param("lat", "33.1111")
                        .param("lng", "128.1111")
                        .param("rating", "1")
                        .param("categories", "['모험', '음식']")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isCreated());
    }

    @Test
    @CustomMockUser
    @DisplayName("장소 생성 실패 - 등록 정보 누락")
    void postPlaceFailMissingInput() throws Exception {
        // given
        MockMultipartFile file1 = new MockMultipartFile("images", "image1.jpg", "image/jpeg", "image1 content".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.png", "image/png", "image2 content".getBytes(StandardCharsets.UTF_8));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/places")
                        .file(file1)
                        .file(file2)
                        .param("description", "Sample description")
                        .param("lat", "33.1111")
                        .param("lng", "128.1111")
                        .param("rating", "1")
                        .param("categories", "['모험', '음식']")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @CustomMockUser
    @DisplayName("장소 생성 실패 - 카테고리 null")
    void postPlaceFailNullCategories() throws Exception {
        // given
        MockMultipartFile file1 = new MockMultipartFile("images", "image1.jpg", "image/jpeg", "image1 content".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.png", "image/png", "image2 content".getBytes(StandardCharsets.UTF_8));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/places")
                        .file(file1)
                        .file(file2)
                        .param("name", "Sample name")
                        .param("description", "Sample description")
                        .param("lat", "33.1111")
                        .param("lng", "128.1111")
                        .param("rating", "1")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isCreated());
    }

    @Test
    @CustomMockUser
    @DisplayName("장소 생성 실패 - 등록 정보 누락")
    void postPlaceFail() throws Exception {
        // given
        MockMultipartFile file1 = new MockMultipartFile("images", "image1.jpg", "image/jpeg", "image1 content".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.png", "image/png", "image2 content".getBytes(StandardCharsets.UTF_8));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/places")
                        .file(file1)
                        .file(file2)
                        .param("description", "Sample description")
                        .param("lat", "33.1111")
                        .param("lng", "128.1111")
                        .param("rating", "1")
                        .param("categories", "['모험', '음식']")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @CustomMockUser
    @DisplayName("장소 생성 실패 - 이미지 없음")
    void postPlaceFailNoImage() throws Exception {
        // expected
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/places")
                        .param("content", "Sample content")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @CustomMockUser
    @DisplayName("장소 생성 실패 - 이미지 너무 많음")
    void postPlaceFailTooManyImages() throws Exception {
        // given
        MockMultipartHttpServletRequestBuilder builder = multipart("/api/v1/places");

        for (int i = 0; i < 11; i++) {
            MockMultipartFile file = new MockMultipartFile(
                    "images",
                    "image" + i + ".jpg",
                    "image/jpeg",
                    ("image" + i + " content").getBytes(StandardCharsets.UTF_8)
            );

            builder.file(file);
        }

        // expected
        mockMvc.perform(builder.param("content", "Sample content")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Nested
    @DisplayName("장소 조회 테스트")
    class GetPlaceListTest {

        @Test
        @CustomMockUser
        @DisplayName("회원 사용자의 장소 조회 - 카테고리가 없는 장소")
        void getListForAuthenticatedUser() throws Exception {
            // given
            Place place = Place.builder()
                    .name("테스트 장소")
                    .description("테스트 설명")
                    .lat(37.5665)
                    .lng(126.9780)
                    .rating(5L)
                    .build();
            Place savedPlace = placeRepository.save(place);

            // expected
            mockMvc.perform(get("/api/v1/places"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size").value(1))
                    .andExpect(jsonPath("$.items[0].placeId").value(savedPlace.getId()))
                    .andExpect(jsonPath("$.items[0].name").value(savedPlace.getName()))
                    .andExpect(jsonPath("$.items[0].lat").value(savedPlace.getLat()))
                    .andExpect(jsonPath("$.items[0].lng").value(savedPlace.getLng()))
                    .andExpect(jsonPath("$.items[0].isBookmarked").value(false))
                    .andExpect(jsonPath("$.items[0].categories").isArray())
                    .andExpect(jsonPath("$.items[0].categories", hasSize(0)));
        }

        @Test
        @CustomMockUser
        @DisplayName("회원 사용자의 장소 조회 - 카테고리가 있는 장소")
        void getListForAuthenticatedUserWithCategories() throws Exception {
            // given
            Category category1 = Category.builder().name("관광").build();
            Category category2 = Category.builder().name("맛집").build();
            List<Category> categories = categoryRepository.saveAll(List.of(category1, category2));

            Place place = Place.builder()
                    .name("테스트 장소")
                    .description("테스트 설명")
                    .lat(37.5665)
                    .lng(126.9780)
                    .rating(5L)
                    .build();
            categories.forEach(place::addCategory);
            Place savedPlace = placeRepository.save(place);

            // expected
            mockMvc.perform(get("/api/v1/places"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size").value(1))
                    .andExpect(jsonPath("$.items[0].placeId").value(savedPlace.getId()))
                    .andExpect(jsonPath("$.items[0].name").value(savedPlace.getName()))
                    .andExpect(jsonPath("$.items[0].lat").value(savedPlace.getLat()))
                    .andExpect(jsonPath("$.items[0].lng").value(savedPlace.getLng()))
                    .andExpect(jsonPath("$.items[0].isBookmarked").value(false))
                    .andExpect(jsonPath("$.items[0].categories").isArray())
                    .andExpect(jsonPath("$.items[0].categories", hasSize(2)))
                    .andExpect(jsonPath("$.items[0].categories[0]").value("관광"))
                    .andExpect(jsonPath("$.items[0].categories[1]").value("맛집"));
        }

        @Test
        @DisplayName("익명 사용자의 장소 조회 - 카테고리가 없는 장소")
        void getListForAnonymousUser() throws Exception {
            // given
            Place place = Place.builder()
                    .name("테스트 장소")
                    .description("테스트 설명")
                    .lat(37.5665)
                    .lng(126.9780)
                    .rating(5L)
                    .build();
            Place savedPlace = placeRepository.save(place);

            // expected
            mockMvc.perform(get("/api/v1/places"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size").value(1))
                    .andExpect(jsonPath("$.items[0].placeId").value(savedPlace.getId()))
                    .andExpect(jsonPath("$.items[0].name").value(savedPlace.getName()))
                    .andExpect(jsonPath("$.items[0].lat").value(savedPlace.getLat()))
                    .andExpect(jsonPath("$.items[0].lng").value(savedPlace.getLng()))
                    .andExpect(jsonPath("$.items[0].isBookmarked").value(false))
                    .andExpect(jsonPath("$.items[0].categories").isArray())
                    .andExpect(jsonPath("$.items[0].categories", hasSize(0)));
        }

        @Test
        @DisplayName("익명 사용자의 장소 조회 - 카테고리가 있는 장소")
        void getListForAnonymousUserWithCategories() throws Exception {
            // given
            Category category1 = Category.builder().name("관광").build();
            Category category2 = Category.builder().name("맛집").build();
            List<Category> categories = categoryRepository.saveAll(List.of(category1, category2));

            Place place = Place.builder()
                    .name("테스트 장소")
                    .description("테스트 설명")
                    .lat(37.5665)
                    .lng(126.9780)
                    .rating(5L)
                    .build();
            categories.forEach(place::addCategory);
            Place savedPlace = placeRepository.save(place);

            // expected
            mockMvc.perform(get("/api/v1/places"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size").value(1))
                    .andExpect(jsonPath("$.items[0].placeId").value(savedPlace.getId()))
                    .andExpect(jsonPath("$.items[0].name").value(savedPlace.getName()))
                    .andExpect(jsonPath("$.items[0].lat").value(savedPlace.getLat()))
                    .andExpect(jsonPath("$.items[0].lng").value(savedPlace.getLng()))
                    .andExpect(jsonPath("$.items[0].isBookmarked").value(false))
                    .andExpect(jsonPath("$.items[0].categories").isArray())
                    .andExpect(jsonPath("$.items[0].categories", hasSize(2)))
                    .andExpect(jsonPath("$.items[0].categories[0]").value("관광"))
                    .andExpect(jsonPath("$.items[0].categories[1]").value("맛집"));
        }

        @Test
        @CustomMockUser
        @DisplayName("회원 사용자의 장소 조회 - 북마크 검사")
        void getListForAuthenticatedUserCheckBookmark() throws Exception {
            // given
            User user = userRepository.findAll().get(0);
            
            Place place1 = Place.builder()
                    .name("테스트 장소1")
                    .description("테스트 설명1")
                    .lat(37.5665)
                    .lng(126.9780)
                    .rating(5L)
                    .build();
            Place place2 = Place.builder()
                    .name("테스트 장소2")
                    .description("테스트 설명2")
                    .lat(37.5665)
                    .lng(126.9780)
                    .rating(5L)
                    .build();
            placeRepository.saveAll(List.of(place1, place2));

            Bookmark bookmark = Bookmark.builder()
                    .user(user)
                    .place(place1)
                    .build();
            bookmarkRepository.save(bookmark);

            // expected
            mockMvc.perform(get("/api/v1/places"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size").value(2))
                    .andExpect(jsonPath("$.items[0].placeId").value(place1.getId()))
                    .andExpect(jsonPath("$.items[0].name").value(place1.getName()))
                    .andExpect(jsonPath("$.items[0].lat").value(place1.getLat()))
                    .andExpect(jsonPath("$.items[0].lng").value(place1.getLng()))
                    .andExpect(jsonPath("$.items[0].isBookmarked").value(true))
                    .andExpect(jsonPath("$.items[1].placeId").value(place2.getId()))
                    .andExpect(jsonPath("$.items[1].name").value(place2.getName()))
                    .andExpect(jsonPath("$.items[1].lat").value(place2.getLat()))
                    .andExpect(jsonPath("$.items[1].lng").value(place2.getLng()))
                    .andExpect(jsonPath("$.items[1].isBookmarked").value(false));
        }

        @Test
        @CustomMockUser
        @DisplayName("회원 사용자의 장소 조회 - 카테고리 키워드로 검색")
        void getListForAuthenticatedUserByCategory() throws Exception {
            // given
            Category category1 = Category.builder().name("관광").build();
            Category category2 = Category.builder().name("맛집").build();
            categoryRepository.saveAll(List.of(category1, category2));

            Place place1 = Place.builder()
                    .name("테스트 장소1")
                    .description("테스트 설명1")
                    .lat(37.5665)
                    .lng(126.9780)
                    .rating(5L)
                    .build();

            Place place2 = Place.builder()
                    .name("테스트 장소2")
                    .description("테스트 설명2")
                    .lat(37.5666)
                    .lng(126.9781)
                    .rating(4L)
                    .build();
            
            Place place3 = Place.builder()
                    .name("테스트 장소3")
                    .description("테스트 설명3")
                    .lat(37.5667)
                    .lng(126.9782)
                    .rating(3L)
                    .build();

            place1.addCategory(category1);
            place2.addCategory(category1);
            place3.addCategory(category2);

            placeRepository.saveAll(List.of(place1, place2, place3));

            // expected
            mockMvc.perform(get("/api/v1/places")
                    .param("category", category1.getName())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size").value(2))
                    .andExpect(jsonPath("$.items", hasSize(2)))
                    .andExpect(jsonPath("$.items[0].placeId").value(place1.getId()))
                    .andExpect(jsonPath("$.items[1].placeId").value(place2.getId()));

            mockMvc.perform(get("/api/v1/places")
                    .param("category", category2.getName())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size").value(1))
                    .andExpect(jsonPath("$.items", hasSize(1)))
                    .andExpect(jsonPath("$.items[0].placeId").value(place3.getId()));
        }

        @Test
        @DisplayName("익명 사용자의 장소 조회 - 카테고리 키워드로 검색")
        void getListForAnonymousByCategory() throws Exception {
            // given
            Category category1 = Category.builder().name("관광").build();
            Category category2 = Category.builder().name("맛집").build();
            categoryRepository.saveAll(List.of(category1, category2));

            Place place1 = Place.builder()
                    .name("테스트 장소1")
                    .description("테스트 설명1")
                    .lat(37.5665)
                    .lng(126.9780)
                    .rating(5L)
                    .build();

            Place place2 = Place.builder()
                    .name("테스트 장소2")
                    .description("테스트 설명2")
                    .lat(37.5666)
                    .lng(126.9781)
                    .rating(4L)
                    .build();
            
            Place place3 = Place.builder()
                    .name("테스트 장소3")
                    .description("테스트 설명3")
                    .lat(37.5667)
                    .lng(126.9782)
                    .rating(3L)
                    .build();

            place1.addCategory(category1);
            place2.addCategory(category1);
            place3.addCategory(category2);

            placeRepository.saveAll(List.of(place1, place2, place3));

            // expected
            mockMvc.perform(get("/api/v1/places")
                    .param("category", category1.getName())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size").value(2))
                    .andExpect(jsonPath("$.items", hasSize(2)))
                    .andExpect(jsonPath("$.items[0].placeId").value(place1.getId()))
                    .andExpect(jsonPath("$.items[1].placeId").value(place2.getId()));

            mockMvc.perform(get("/api/v1/places")
                    .param("category", category2.getName())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size").value(1))
                    .andExpect(jsonPath("$.items", hasSize(1)))
                    .andExpect(jsonPath("$.items[0].placeId").value(place3.getId()));
        }

        @Test
        @CustomMockUser
        @DisplayName("장소 id로 상세 정보 조회")
        void getPlaceById() throws Exception {
            // given
            Category category = Category.builder().name("관광").build();
            categoryRepository.save(category);

            Place place = Place.builder()
                    .name("테스트 장소")
                    .description("테스트 설명")
                    .lat(37.5665)
                    .lng(126.9780)
                    .rating(5L)
                    .build();
            place.addCategory(category);
            Place savedPlace = placeRepository.save(place);

            // expected
            mockMvc.perform(get("/api/v1/places/{placeId}", savedPlace.getId())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.placeId").value(savedPlace.getId()))
                    .andExpect(jsonPath("$.name").value(savedPlace.getName()))
                    .andExpect(jsonPath("$.description").value(savedPlace.getDescription()))
                    .andExpect(jsonPath("$.lat").value(savedPlace.getLat()))
                    .andExpect(jsonPath("$.lng").value(savedPlace.getLng()))
                    .andExpect(jsonPath("$.rating").value(savedPlace.getRating()))
                    .andExpect(jsonPath("$.isBookmarked").value(false))
                    .andExpect(jsonPath("$.categories").isArray())
                    .andExpect(jsonPath("$.categories", hasSize(1)))
                    .andExpect(jsonPath("$.categories[0]").value("관광"))
                    .andExpect(jsonPath("$.imageUrls").isArray())
                    .andExpect(jsonPath("$.imageUrls", hasSize(0)));
        }

        @Test
        @CustomMockUser
        @DisplayName("카테고리와 이미지가 모두 있는 장소 상세 정보 조회")
        void getPlaceWithCategoriesAndImages() throws Exception {
            // given
            Category category1 = Category.builder().name("관광").build();
            Category category2 = Category.builder().name("맛집").build();
            categoryRepository.saveAll(List.of(category1, category2));

            Place place = Place.builder()
                    .name("테스트 장소")
                    .description("테스트 설명")
                    .lat(37.5665)
                    .lng(126.9780)
                    .rating(4L)
                    .build();
            place.addCategory(category1);
            place.addCategory(category2);

            Image image1 = Image.builder().url("http://example.com/image1.jpg").build();
            Image image2 = Image.builder().url("http://example.com/image2.jpg").build();
            place.addImage(image1);
            place.addImage(image2);

            Place savedPlace = placeRepository.save(place);

            // expected
            mockMvc.perform(get("/api/v1/places/{placeId}", savedPlace.getId())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.placeId").value(savedPlace.getId()))
                    .andExpect(jsonPath("$.name").value(savedPlace.getName()))
                    .andExpect(jsonPath("$.description").value(savedPlace.getDescription()))
                    .andExpect(jsonPath("$.lat").value(savedPlace.getLat()))
                    .andExpect(jsonPath("$.lng").value(savedPlace.getLng()))
                    .andExpect(jsonPath("$.rating").value(savedPlace.getRating()))
                    .andExpect(jsonPath("$.isBookmarked").value(false))
                    .andExpect(jsonPath("$.categories").isArray())
                    .andExpect(jsonPath("$.categories", hasSize(2)))
                    .andExpect(jsonPath("$.categories", containsInAnyOrder("관광", "맛집")))
                    .andExpect(jsonPath("$.imageUrls").isArray())
                    .andExpect(jsonPath("$.imageUrls", hasSize(2)))
                    .andExpect(jsonPath("$.imageUrls", containsInAnyOrder(
                            "http://example.com/image1.jpg",
                            "http://example.com/image2.jpg"
                    )));
        }

        @Test
        @CustomMockUser
        @DisplayName("북마크된 장소 목록 조회")
        void getBookmarkedPlaces() throws Exception {
            // given
            User user = userRepository.findAll().get(0);

            Place place1 = Place.builder()
                    .name("북마크된 장소1")
                    .description("설명1")
                    .lat(37.5665)
                    .lng(126.9780)
                    .rating(4L)
                    .build();
            Place place2 = Place.builder()
                    .name("북마크된 장소2")
                    .description("설명2")
                    .lat(37.5667)
                    .lng(126.9782)
                    .rating(5L)
                    .build();

            Image image1 = Image.builder().url("http://example.com/image1.jpg").build();
            Image image2 = Image.builder().url("http://example.com/image2.jpg").build();
            place1.addImage(image1);
            place2.addImage(image2);

            placeRepository.saveAll(List.of(place1, place2));

            Bookmark bookmark1 = Bookmark.builder().user(user).place(place1).build();
            Bookmark bookmark2 = Bookmark.builder().user(user).place(place2).build();
            bookmarkRepository.saveAll(List.of(bookmark1, bookmark2));

            // expected
            mockMvc.perform(get("/api/v1/places/bookmarks")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.items").isArray())
                    .andExpect(jsonPath("$.items.length()").value(2))
                    .andExpect(jsonPath("$.items[0].imageUrls").isArray())
                    .andExpect(jsonPath("$.items[0].imageUrls", hasSize(1)))
                    .andExpect(jsonPath("$.items[0].imageUrls[0]").value("http://example.com/image1.jpg"))
                    .andExpect(jsonPath("$.items[1].imageUrls").isArray())
                    .andExpect(jsonPath("$.items[1].imageUrls", hasSize(1)))
                    .andExpect(jsonPath("$.items[1].imageUrls[0]").value("http://example.com/image2.jpg"));
        }

        @Test
        @CustomMockUser
        @DisplayName("북마크가 없을 때 북마크된 장소 목록 조회")
        void getBookmarkedPlacesWhenEmpty() throws Exception {
            // given
            // 북마크를 등록하지 않음

            // expected
            mockMvc.perform(get("/api/v1/places/bookmarks")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size").value(0))
                    .andExpect(jsonPath("$.items").isArray())
                    .andExpect(jsonPath("$.items").isEmpty());
        }

        @Test
        @DisplayName("비회원이 북마크된 장소 목록 조회 시 인증 오류")
        void getBookmarkedPlacesWhenUnauthorized() throws Exception {
            // given
            // 인증되지 않은 사용자

            // expected
            mockMvc.perform(get("/api/v1/places/bookmarks")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }
}