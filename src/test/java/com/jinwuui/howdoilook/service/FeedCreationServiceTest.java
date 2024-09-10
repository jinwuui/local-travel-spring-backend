package com.jinwuui.howdoilook.service;

import com.jinwuui.howdoilook.config.CustomMockUser;
import com.jinwuui.howdoilook.domain.Feed;
import com.jinwuui.howdoilook.domain.Image;
import com.jinwuui.howdoilook.domain.User;
import com.jinwuui.howdoilook.dto.service.FeedDto;
import com.jinwuui.howdoilook.exception.FeedNotFoundException;
import com.jinwuui.howdoilook.exception.UserNotFoundException;
import com.jinwuui.howdoilook.repository.FeedRepository;
import com.jinwuui.howdoilook.repository.ImageRepository;
import com.jinwuui.howdoilook.repository.S3Repository;
import com.jinwuui.howdoilook.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FeedCreationServiceTest {

    @Autowired
    private FeedCreationService feedCreationService;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private S3Repository s3Repository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clean() {
        feedRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("피드와 이미지 생성")
    void createFeed() {
        // given
        User user = User.builder()
                .email("jinwuui@gmail.com")
                .nickname("jinwuui")
                .password("1234")
                .build();
        userRepository.save(user);

        MockMultipartFile file1 = new MockMultipartFile("images", "image1.jpg", "image/jpeg", "image1 content".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.jpg", "image/jpeg", "image2 content".getBytes(StandardCharsets.UTF_8));
        FeedDto feedDto = FeedDto.builder()
                .content("임시 내용")
                .images(List.of(file1, file2))
                .build();

        // when
        Long feedId = feedCreationService.createFeedWithImages(user.getId(), feedDto);

        // then
        assertEquals(1L, feedRepository.count());
        assertEquals(2L, imageRepository.count());
        Feed feed = feedRepository.findAll().get(0);
        assertEquals(feedId, feed.getId());
        assertEquals(feedDto.getContent(), feed.getContent());
    }

    @Test
    @DisplayName("피드와 이미지 생성 실패 - 유저 찾기 실패")
    void createFeedFailUserNotFound() {
        // given
        User user = User.builder()
                .email("jinwuui@gmail.com")
                .nickname("jinwuui")
                .password("1234")
                .build();
        userRepository.save(user);

        MockMultipartFile file1 = new MockMultipartFile("images", "image1.jpg", "image/jpeg", "image1 content".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.jpg", "image/jpeg", "image2 content".getBytes(StandardCharsets.UTF_8));
        FeedDto feedDto = FeedDto.builder()
                .content("임시 내용")
                .images(List.of(file1, file2))
                .build();

        // expected
        assertThrows(UserNotFoundException.class,
                () -> feedCreationService.createFeedWithImages(user.getId() + 1L, feedDto));
    }

    @Test
    @DisplayName("피드와 이미지 생성 실패 - 이미지가 아닌 파일")
    void createFeedFailNotImageFile() {
        // given
        User user = User.builder()
                .email("jinwuui@gmail.com")
                .nickname("jinwuui")
                .password("1234")
                .build();
        userRepository.save(user);

        MockMultipartFile file1 = new MockMultipartFile("notImages", "notImages.txt", "text/plain", "notImages content".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2 = new MockMultipartFile("images", "image2.jpg", "image/jpeg", "image2 content".getBytes(StandardCharsets.UTF_8));
        FeedDto feedDto = FeedDto.builder()
                .content("임시 내용")
                .images(List.of(file1, file2))
                .build();

        // when
        feedCreationService.createFeedWithImages(user.getId(), feedDto);

        // then
        assertEquals(1L, imageRepository.count());
    }
}