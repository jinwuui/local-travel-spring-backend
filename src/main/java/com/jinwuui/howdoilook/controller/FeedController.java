package com.jinwuui.howdoilook.controller;

import com.jinwuui.howdoilook.config.UserPrincipal;
import com.jinwuui.howdoilook.dto.request.FeedCreateRequest;
import com.jinwuui.howdoilook.service.FeedCreationService;
import com.jinwuui.howdoilook.service.FeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.jinwuui.howdoilook.dto.mapper.FeedMapper.*;

@RestController
@RequestMapping("/api/v1/feeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    private final FeedCreationService feedCreationService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Long post(@AuthenticationPrincipal UserPrincipal userPrincipal, @ModelAttribute @Valid FeedCreateRequest feedCreateRequest) {
        return feedCreationService.createFeedWithImages(userPrincipal.getUserId(), toFeedDto(feedCreateRequest));
    }

    @GetMapping("/{feedId}")
    public String get(@PathVariable String feedId) {
        return "hi im feed";
    }

    @GetMapping()
    public String getList() {
        return "hi im feed";
    }

}
