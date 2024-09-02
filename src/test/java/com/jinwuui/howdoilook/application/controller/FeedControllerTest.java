package com.jinwuui.howdoilook.application.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class FeedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("피드 조회 요청 테스트")
    void test() throws Exception {
        mockMvc.perform(get("/api/v1/feeds"))
                .andExpect(status().isOk())
                .andExpect(content().string("hi im feed"));
    }

    @Test
    @DisplayName("피드 생성 요청 테스트")
    void createFeed() throws Exception {
        mockMvc.perform(post("/api/v1/feeds"))
                .andExpect(status().isCreated())
                .andExpect(content().string("create feed"));
    }
}