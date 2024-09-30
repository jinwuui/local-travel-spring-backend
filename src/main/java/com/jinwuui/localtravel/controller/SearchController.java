package com.jinwuui.localtravel.controller;

import static com.jinwuui.localtravel.dto.mapper.SearchMapper.toPagingAutocompleteResponse;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jinwuui.localtravel.dto.response.AutocompleteResponse;
import com.jinwuui.localtravel.dto.response.PagingResponse;
import com.jinwuui.localtravel.service.SearchService;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/searches")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/autocomplete")
    public PagingResponse<AutocompleteResponse> getAutocomplete(@RequestParam @NotBlank String keyword) {
        return toPagingAutocompleteResponse(searchService.getAutocompleteResults(keyword));
    }
}
