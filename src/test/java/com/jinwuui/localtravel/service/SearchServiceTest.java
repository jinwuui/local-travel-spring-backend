package com.jinwuui.localtravel.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.jinwuui.localtravel.dto.service.AutocompleteDto;
import com.jinwuui.localtravel.domain.Place;
import com.jinwuui.localtravel.repository.PlaceRepository;
import com.jinwuui.localtravel.util.EmbeddingUtil;

import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@Slf4j
@SpringBootTest
@Transactional
public class SearchServiceTest {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private SearchService searchService;

    @Autowired
    private EmbeddingUtil embeddingUtil;

    @Test
    @DisplayName("검색 자동 완성 테스트 - 유사도 기준값 테스트")
    void getAutocompleteThreshold() {
        // given
        // 츠케맨 장소가 등록되어 있을 때 '라멘'을 입력했을때 연관 검색어로 등장할 것
        Place tsukemen = Place.builder()
                .name("구마모토 동네 츠케맨집")
                .description("처음 츠케맨을 먹은 이후로 이렇게 맛있는 집은 처음입니다. 국물이 진하고, 특히 면이 정말 맛있습니다.")
                .country("일본")
                .lat(32.79640727633594)
                .lng(130.71849453454735)
                .rating(5L)
                .build();
        
        tsukemen.setEmbedding(embeddingUtil.fetchEmbedding(tsukemen.getEmbeddingText()));

        placeRepository.save(tsukemen);

        // when
        log.info(">>> {}", tsukemen.getEmbeddingText());
        List<AutocompleteDto> results = searchService.getAutocompleteResults("tsukemen");

        // then
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    @DisplayName("검색 자동 완성 테스트")
    void getAutocompleteResults() {
        // given
        Place place1 = Place.builder()
                .name("서울특별시")
                .description("대한민국의 수도")
                .country("대한민국")
                .lat(37.5665)
                .lng(126.9780)
                .rating(5L)
                .build();
        Place place2 = Place.builder()
                .name("서울역")
                .description("서울의 중심 기차역")
                .country("대한민국")
                .lat(37.5546)
                .lng(126.9706)
                .rating(4L)
                .build();
        Place place3 = Place.builder()
                .name("서울타워")
                .description("서울의 랜드마크")
                .country("대한민국")
                .lat(37.5511)
                .lng(126.9882)
                .rating(5L)
                .build();

        place1.setEmbedding(embeddingUtil.fetchEmbedding(place1.getEmbeddingText()));
        place2.setEmbedding(embeddingUtil.fetchEmbedding(place2.getEmbeddingText()));
        place3.setEmbedding(embeddingUtil.fetchEmbedding(place3.getEmbeddingText()));

        placeRepository.saveAll(List.of(place1, place2, place3));

        // when
        List<AutocompleteDto> results = searchService.getAutocompleteResults("서울");

        // then
        assertNotNull(results);
        assertEquals(3, results.size());
        
        assertTrue(results.stream().anyMatch(dto -> dto.getName().equals("서울특별시")));
        assertTrue(results.stream().anyMatch(dto -> dto.getName().equals("서울역")));
        assertTrue(results.stream().anyMatch(dto -> dto.getName().equals("서울타워")));
        
        assertTrue(results.stream().allMatch(dto -> dto.getCountry().equals("대한민국")));
    }

    @Test
    @DisplayName("검색 자동 완성 테스트")
    void getAutocompleteResultsWithSimilarData() {
        // given
        Place place1 = Place.builder()
                .name("서울특별시")
                .description("대한민국의 수도")
                .country("대한민국")
                .lat(37.5665)
                .lng(126.9780)
                .rating(5L)
                .build();
        Place place2 = Place.builder()
                .name("서울역")
                .description("서울의 중심 기차역")
                .country("대한민국")
                .lat(37.5546)
                .lng(126.9706)
                .rating(4L)
                .build();
        Place place3 = Place.builder()
                .name("서울타워")
                .description("서울의 랜드마크")
                .country("대한민국")
                .lat(37.5511)
                .lng(126.9882)
                .rating(5L)
                .build();
        Place place4 = Place.builder()
                .name("부산광역시")
                .description("대한민국의 해양도시")
                .country("대한민국")
                .lat(35.1796)
                .lng(129.0756)
                .rating(4L)
                .build();
        Place place5 = Place.builder()
                .name("나고야")
                .description("일본의 대도시")
                .country("일본")
                .lat(35.172340)
                .lng(136.908325)
                .rating(4L)
                .build();
                        
        place1.setEmbedding(embeddingUtil.fetchEmbedding(place1.getEmbeddingText()));
        place2.setEmbedding(embeddingUtil.fetchEmbedding(place2.getEmbeddingText()));
        place3.setEmbedding(embeddingUtil.fetchEmbedding(place3.getEmbeddingText()));
        place4.setEmbedding(embeddingUtil.fetchEmbedding(place4.getEmbeddingText()));
        place5.setEmbedding(embeddingUtil.fetchEmbedding(place5.getEmbeddingText()));
        
        placeRepository.saveAll(List.of(place1, place2, place3, place4, place5));

        // when
        List<AutocompleteDto> results = searchService.getAutocompleteResults("서울");

        // then
        assertNotNull(results);
        assertEquals(3, results.size());
        
        assertTrue(results.stream().anyMatch(dto -> dto.getName().equals("서울특별시")));
        assertTrue(results.stream().anyMatch(dto -> dto.getName().equals("서울역")));
        assertTrue(results.stream().anyMatch(dto -> dto.getName().equals("서울타워")));
        
        assertFalse(results.stream().anyMatch(dto -> dto.getName().equals("나고야")));
        assertFalse(results.stream().anyMatch(dto -> dto.getName().equals("부산광역시")));
        assertTrue(results.stream().allMatch(dto -> dto.getCountry().equals("대한민국")));
    }
}
