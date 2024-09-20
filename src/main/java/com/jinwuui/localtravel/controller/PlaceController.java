package com.jinwuui.localtravel.controller;

import com.jinwuui.localtravel.config.UserPrincipal;
import com.jinwuui.localtravel.dto.request.PlaceCreateRequest;
import com.jinwuui.localtravel.service.PlaceCreationService;
import com.jinwuui.localtravel.service.PlaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.jinwuui.localtravel.dto.mapper.PlaceMapper.*;

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

    @GetMapping("/{placeId}")
    public String get(@PathVariable String placeId) {
        return "hi im place";
    }

    @GetMapping()
    public String getList() {
        return "hi im place";
    }

}
