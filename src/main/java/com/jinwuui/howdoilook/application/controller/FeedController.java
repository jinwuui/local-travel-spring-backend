package com.jinwuui.howdoilook.application.controller;

import com.jinwuui.howdoilook.application.domain.FeedCreateReq;
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
    public void createFeed(@ModelAttribute FeedCreateReq feedCreateReq) {

    }
}
