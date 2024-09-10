package com.jinwuui.howdoilook.controller;

import com.jinwuui.howdoilook.config.CustomMockUser;
import com.jinwuui.howdoilook.repository.FeedRepository;
import com.jinwuui.howdoilook.repository.S3Repository;
import com.jinwuui.howdoilook.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class FeedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clean() {
        feedRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @CustomMockUser
    @DisplayName("피드 조회")
    void test() throws Exception {
        mockMvc.perform(get("/api/v1/feeds"))
                .andExpect(status().isOk())
                .andExpect(content().string("hi im feed"));
    }

    @Test
    @CustomMockUser
    @DisplayName("피드 생성")
    void postFeed() throws Exception {
        // given
        MockMultipartFile file1 = new MockMultipartFile("images", "image1.jpg", "image/jpeg", "image1 content".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.png", "image/png", "image2 content".getBytes(StandardCharsets.UTF_8));

        // expected
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/feeds")
                        .file(file1)
                        .file(file2)
                        .param("content", "Sample content")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isCreated());
    }

    @Test
    @CustomMockUser
    @DisplayName("피드 생성 실패 - 이미지 없음")
    void postFeedFailNoImage() throws Exception {
        // expected
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/feeds")
                        .param("content", "Sample content")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @CustomMockUser
    @DisplayName("피드 생성 실패 - 이미지 너무 많음")
    void postFeedFailTooManyImages() throws Exception {
        // given
        MockMultipartHttpServletRequestBuilder builder = multipart("/api/v1/feeds");

        for (int i = 0; i < 11; i++) {
            MockMultipartFile file = new MockMultipartFile(
                    "images",
                    "image" + i + ".jpg",
                    "image/jpeg",
                    ("image" + i + " content").getBytes(StandardCharsets.UTF_8)
            );

            builder.file(file);
        }

        // expected
        mockMvc.perform(builder.param("content", "Sample content")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }
}