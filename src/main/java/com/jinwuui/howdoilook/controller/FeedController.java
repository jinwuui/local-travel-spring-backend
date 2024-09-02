package com.jinwuui.howdoilook.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/feeds")
public class FeedController {

    @GetMapping()
    public String getFeed() {
        return "hi im feed";
    }
}
