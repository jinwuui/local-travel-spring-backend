package com.jinwuui.howdoilook.controller;

import com.jinwuui.howdoilook.dto.request.FeedCreateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feeds")
public class FeedController {

    @GetMapping()
    public String getFeed() {
        return "hi im feed";
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String createFeed(@ModelAttribute FeedCreateRequest feedCreateRequest) {
        return "create feed";
    }
}
