package com.jinwuui.localtravel.controller;

import com.jinwuui.localtravel.config.UserPrincipal;
import com.jinwuui.localtravel.dto.request.PlaceCreateRequest;
import com.jinwuui.localtravel.dto.response.PagingResponse;
import com.jinwuui.localtravel.dto.response.PlaceResponse;
import com.jinwuui.localtravel.service.PlaceCreationService;
import com.jinwuui.localtravel.service.PlaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.jinwuui.localtravel.dto.mapper.PlaceMapper.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    private final PlaceCreationService placeCreationService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Long post(@AuthenticationPrincipal UserPrincipal userPrincipal, @ModelAttribute @Valid PlaceCreateRequest placeCreateRequest) {
        return placeCreationService.createPlaceWithImages(userPrincipal.getUserId(), toPlaceDto(placeCreateRequest));
    }

    @GetMapping()
    public PagingResponse<PlaceResponse> getList(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal != null) {
            placeService.readForUser(userPrincipal.getUserId());
        } else {
            placeService.readForAnonymous();
        }
        return null;
    }

    @GetMapping(params = "category")
    public PagingResponse<PlaceResponse> getListByCategory(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam(name = "category") String category) {
        if (userPrincipal != null) { 
            placeService.readForUserByCategory(userPrincipal.getUserId(), category);
        } else { 
            placeService.readForAnonymousByCategory(category);
        }
        return null;
        // return placeService.readPlaceListForUser(userPrincipal.getUserId(), category);
    }

    @GetMapping("/{placeId}")
    public String get(@PathVariable String placeId) {
        return "hi im place";
    }
}
