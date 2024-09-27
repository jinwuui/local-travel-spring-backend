package com.jinwuui.localtravel.controller;

import com.jinwuui.localtravel.config.UserPrincipal;
import com.jinwuui.localtravel.dto.request.PlaceCreateRequest;
import com.jinwuui.localtravel.dto.response.BookmarkStatusResponse;
import com.jinwuui.localtravel.dto.response.BookmarkedPlaceResponse;
import com.jinwuui.localtravel.dto.response.PagingResponse;
import com.jinwuui.localtravel.dto.response.PlaceDetailResponse;
import com.jinwuui.localtravel.dto.response.PlaceResponse;
import com.jinwuui.localtravel.service.PlaceCreationService;
import com.jinwuui.localtravel.service.PlaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.jinwuui.localtravel.dto.mapper.PlaceMapper.*;

import java.util.Optional;

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
            return toPagingPlaceResponse(placeService.readForUser(userPrincipal.getUserId()));
        } else {
            return toPagingPlaceResponse(placeService.readForAnonymous());
        }
    }

    @GetMapping(params = "category")
    public PagingResponse<PlaceResponse> getListByCategory(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam String category) {
        if (userPrincipal != null) {
            return toPagingPlaceResponse(placeService.readForUserByCategory(userPrincipal.getUserId(), category));
        } else {
            return toPagingPlaceResponse(placeService.readForAnonymousByCategory(category));
        }
    }

    @GetMapping("/{placeId}")
    public PlaceDetailResponse get(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable String placeId) {
        Optional<Long> optionalUserId = Optional.ofNullable(userPrincipal.getUserId());
        return toPlaceDetailResponse(placeService.read(optionalUserId, Long.parseLong(placeId)));
    }

    @GetMapping("/bookmarks")
    @PreAuthorize("hasRole('ROLE_USER')")
    public PagingResponse<BookmarkedPlaceResponse> getBookmarkList(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return toBookmarkedPlaceResponse(placeService.readBookmarks(userPrincipal.getUserId()));
    }

    @PostMapping("/bookmarks/{placeId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public BookmarkStatusResponse toggleBookmark(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long placeId) {
        boolean isBookmarked = placeService.toggleBookmark(userPrincipal.getUserId(), placeId);
        return BookmarkStatusResponse.builder()
                .userId(userPrincipal.getUserId())
                .placeId(placeId)
                .isBookmarked(isBookmarked)
                .build();
    }
}
