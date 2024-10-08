package com.jinwuui.localtravel.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinwuui.localtravel.domain.Announcement;
import com.jinwuui.localtravel.domain.Feedback;
import com.jinwuui.localtravel.dto.request.FeedbackRequest;
import com.jinwuui.localtravel.repository.AnnouncementRepository;
import com.jinwuui.localtravel.repository.FeedbackRepository;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.ArrayList;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class CommunicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @AfterEach
    void clean() {
        announcementRepository.deleteAll();
        feedbackRepository.deleteAll();
    }

    @Test
    @DisplayName("공지사항 조회 테스트")
    void getAnnouncements() throws Exception {
        // given
        announcementRepository.saveAll(List.of(
                Announcement.builder().version("1.0.0").content("공지사항 1").build(),
                Announcement.builder().version("1.0.1").content("공지사항 2").build()));

        // expected
        mockMvc.perform(get("/api/v1/communications/announcements")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].version").value("1.0.1"))
                .andExpect(jsonPath("$.items[0].content").value("공지사항 2"))
                .andExpect(jsonPath("$.items[1].version").value("1.0.0"))
                .andExpect(jsonPath("$.items[1].content").value("공지사항 1"));
    }

    @Test
    @DisplayName("공지사항 10개 이상 등록 시 최근 10개만 조회되는지 테스트")
    void getLatestTenAnnouncements() throws Exception {
        // given
        List<Announcement> announcements = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            announcements.add(Announcement.builder()
                    .version("1.0." + i)
                    .content("공지사항 " + i)
                    .build());
        }
        announcementRepository.saveAll(announcements);

        // expected
        mockMvc.perform(get("/api/v1/communications/announcements")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(10)))
                .andExpect(jsonPath("$.items[0].version").value("1.0.15"))
                .andExpect(jsonPath("$.items[0].content").value("공지사항 15"))
                .andExpect(jsonPath("$.items[9].version").value("1.0.6"))
                .andExpect(jsonPath("$.items[9].content").value("공지사항 6"));
    }

    @Test
    @DisplayName("공지사항이 없을 때 빈 배열 반환 테스트")
    void getEmptyAnnouncementList() throws Exception {
        // given
        // 공지사항을 추가하지 않음

        // expected
        mockMvc.perform(get("/api/v1/communications/announcements")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items").isEmpty());
    }

    @Test
    @DisplayName("피드백 등록")
    void postFeedback() throws Exception {
        // given
        FeedbackRequest feedbackRequest = FeedbackRequest.builder()
                .writer("테스트 작성자")
                .content("테스트 피드백 내용")
                .build();

        // when & then
        mockMvc.perform(post("/api/v1/communications/feedbacks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(feedbackRequest)))
                .andExpect(status().isCreated());

        List<Feedback> savedFeedbacks = feedbackRepository.findAll();
        assertThat(savedFeedbacks).hasSize(1);
        assertThat(savedFeedbacks.get(0).getWriter()).isEqualTo("테스트 작성자");
        assertThat(savedFeedbacks.get(0).getContent()).isEqualTo("테스트 피드백 내용");
    }

    @Test
    @DisplayName("작성자가 없는 피드백 등록 성공")
    void postFeedbackWithoutWriter() throws Exception {
        // given
        FeedbackRequest feedbackRequest = FeedbackRequest.builder()
                .content("작성자 없는 피드백 내용")
                .build();

        // when & then
        mockMvc.perform(post("/api/v1/communications/feedbacks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(feedbackRequest)))
                .andExpect(status().isCreated());

        List<Feedback> savedFeedbacks = feedbackRepository.findAll();
        assertThat(savedFeedbacks).hasSize(1);
        assertThat(savedFeedbacks.get(0).getWriter()).isNull();
        assertThat(savedFeedbacks.get(0).getContent()).isEqualTo("작성자 없는 피드백 내용");
    }

    @Test
    @DisplayName("내용이 없는 피드백 등록 실패")
    void postFeedbackWithoutContent() throws Exception {
        // given
        FeedbackRequest feedbackRequest = FeedbackRequest.builder()
                .writer("테스트 작성자")
                .build();

        // when & then
        mockMvc.perform(post("/api/v1/communications/feedbacks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(feedbackRequest)))
                .andExpect(status().isBadRequest());

        List<Feedback> savedFeedbacks = feedbackRepository.findAll();
        assertThat(savedFeedbacks).isEmpty();
    }
}
