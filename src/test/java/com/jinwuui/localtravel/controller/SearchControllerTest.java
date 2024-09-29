package com.jinwuui.localtravel.controller;

import com.jinwuui.localtravel.domain.Place;
import com.jinwuui.localtravel.repository.PlaceRepository;
import com.jinwuui.localtravel.util.EmbeddingUtil;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private EmbeddingUtil embeddingUtil;

    @AfterEach
    void clean() {
        placeRepository.deleteAll();
    }

    @Test
    @DisplayName("유사도 기반 검색 자동 완성 - 키워드 서울")
    void getAutocompleteKeywordSeoul() throws Exception {
        // given
        // 서울 관련 장소 등록
        Place seoulPlace = Place.builder()
                .name("서울특별시")
                .description("대한민국의 수도")
                .country("대한민국")
                .lat(37.5665)
                .lng(126.9780)
                .rating(5L)
                .build();
        seoulPlace.setEmbedding(embeddingUtil.fetchEmbedding(seoulPlace.getEmbeddingText()));

        // 부산 관련 장소 등록
        Place busanPlace = Place.builder()
                .name("부산광역시")
                .description("대한민국의 해양도시")
                .country("대한민국")
                .lat(35.1796)
                .lng(129.0756)
                .rating(4L)
                .build();
        busanPlace.setEmbedding(embeddingUtil.fetchEmbedding(busanPlace.getEmbeddingText()));

        // 일본 관련 장소 등록
        Place japanPlace = Place.builder()
                .name("도쿄시")
                .description("일본의 수도")
                .country("일본")
                .lat(35.681236)
                .lng(139.767125)
                .rating(5L)
                .build();
        japanPlace.setEmbedding(embeddingUtil.fetchEmbedding(japanPlace.getEmbeddingText()));

        // 베트남 관련 장소 등록
        Place vietnamPlace = Place.builder()
                .name("호치민시")
                .description("베트남의 최대 도시")
                .country("베트남")
                .lat(10.8231)
                .lng(106.6297)
                .rating(4L)
                .build();
        vietnamPlace.setEmbedding(embeddingUtil.fetchEmbedding(vietnamPlace.getEmbeddingText()));

        placeRepository.saveAll(List.of(seoulPlace, busanPlace, japanPlace, vietnamPlace));

        // expected
        mockMvc.perform(get("/api/v1/searches/autocomplete")
                .param("keyword", "서울")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size").value(2L))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].name").value("서울특별시"))
                .andExpect(jsonPath("$.items[1].name").value("도쿄시"));
    }

    @Test
    @DisplayName("유사도 기반 검색 자동 완성 - 키워드 한국")
    void getAutocompleteKeywordKorea() throws Exception {
        // given
        // 서울 관련 장소 등록
        Place seoulPlace = Place.builder()
                .name("서울특별시")
                .description("대한민국의 수도")
                .country("대한민국")
                .lat(37.5665)
                .lng(126.9780)
                .rating(5L)
                .build();
        seoulPlace.setEmbedding(embeddingUtil.fetchEmbedding(seoulPlace.getEmbeddingText()));

        // 부산 관련 장소 등록
        Place busanPlace = Place.builder()
                .name("부산광역시")
                .description("대한민국의 해양도시")
                .country("대한민국")
                .lat(35.1796)
                .lng(129.0756)
                .rating(4L)
                .build();
        busanPlace.setEmbedding(embeddingUtil.fetchEmbedding(busanPlace.getEmbeddingText()));

        // 일본 관련 장소 등록
        Place japanPlace = Place.builder()
                .name("도쿄시")
                .description("일본의 수도")
                .country("일본")
                .lat(35.681236)
                .lng(139.767125)
                .rating(5L)
                .build();
        japanPlace.setEmbedding(embeddingUtil.fetchEmbedding(japanPlace.getEmbeddingText()));

        // 베트남 관련 장소 등록
        Place vietnamPlace = Place.builder()
                .name("호치민시")
                .description("베트남의 최대 도시")
                .country("베트남")
                .lat(10.8231)
                .lng(106.6297)
                .rating(4L)
                .build();
        vietnamPlace.setEmbedding(embeddingUtil.fetchEmbedding(vietnamPlace.getEmbeddingText()));

        placeRepository.saveAll(List.of(seoulPlace, busanPlace, japanPlace, vietnamPlace));

        // expected
        mockMvc.perform(get("/api/v1/searches/autocomplete")
                .param("keyword", "한국")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size").value(2L))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].name").value("서울특별시"))
                .andExpect(jsonPath("$.items[1].name").value("부산광역시"));
    }

    @Test
    public void testEmptyKeyword() throws Exception {
        // expected
        mockMvc.perform(get("/api/v1/searches/autocomplete")
                .param("keyword", "")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        mockMvc.perform(get("/api/v1/searches/autocomplete")
                .param("keyword", "  ")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
