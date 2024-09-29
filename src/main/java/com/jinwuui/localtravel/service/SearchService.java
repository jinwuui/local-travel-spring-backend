package com.jinwuui.localtravel.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jinwuui.localtravel.domain.Place;
import com.jinwuui.localtravel.dto.service.AutocompleteDto;
import com.jinwuui.localtravel.repository.PlaceRepository;
import com.jinwuui.localtravel.util.EmbeddingUtil;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    @Value("${autocomplete.limit}")
    private int limit;

    @Value("${autocomplete.similarity-threshold}")
    private double similarityThreshold;

    private final PlaceRepository placeRepository;

    private final EmbeddingUtil embeddingUtil;
    
    public List<AutocompleteDto> getAutocompleteResults(String keyword) {
        String trimmedKeyword = keyword.trim();
        
        List<Double> embedding = embeddingUtil.fetchEmbedding(trimmedKeyword);
        String value = embedding.stream()
                                .map(Object::toString)
                                .collect(Collectors.joining(","));
        value = "[" + value + "]";

        List<Place> similarPlaces = placeRepository.findSimilarPlaces(value, similarityThreshold);
        
        return similarPlaces.stream()
                .map((Place place) -> AutocompleteDto.builder()
                        .placeId(place.getId())
                        .name(place.getName())
                        .description(place.getDescription())
                        .country(place.getCountry())
                        .build())
                .collect(Collectors.toList());
    }
}
