package com.jinwuui.localtravel.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jinwuui.localtravel.repository.AnnouncementRepository;

import lombok.extern.slf4j.Slf4j;

import com.jinwuui.localtravel.domain.Announcement;
import com.jinwuui.localtravel.dto.service.AnnouncementDto;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class CommunicationServiceTest {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private CommunicationService communicationService;

    @AfterEach
    void clean() {
        announcementRepository.deleteAll();
    }

    @Test
    @DisplayName("공지사항 조회")
    void readAnnouncements() {
        // given
        Announcement announcement1 = Announcement.builder()
                .version("1.0.0")
                .content("공지사항 1")
                .build();
        Announcement announcement2 = Announcement.builder()
                .version("1.0.1")
                .content("공지사항 2")
                .build();

        announcementRepository.saveAll(List.of(announcement1, announcement2));
        
        // when
        List<AnnouncementDto> result = communicationService.readAnnouncements();

        log.info(">>> {}", result.get(0).getCreatedAt());
        log.info(">>> {}", result.get(1).getCreatedAt());

        // expected
        assertEquals(2, result.size());
        assertEquals("공지사항 2", result.get(0).getContent());
        assertEquals("공지사항 1", result.get(1).getContent());
    }

    @Test
    @DisplayName("최근 등록된 10개의 공지사항만 조회")
    void readLatestTenAnnouncements() {
        // given
        List<Announcement> announcements = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            announcements.add(Announcement.builder()
                    .version("1.0." + i)
                    .content("공지사항 " + i)
                    .build());
        }
        announcementRepository.saveAll(announcements);

        // when
        List<AnnouncementDto> result = communicationService.readAnnouncements();

        // then
        assertEquals(10, result.size());
        for (int i = 0; i < 10; i++) {
            assertEquals("공지사항 " + (15 - i), result.get(i).getContent());
        }
    }

    @Test
    @DisplayName("공지사항이 없을 때 빈 배열 반환")
    void readEmptyAnnouncements() {
        // given
        // 공지사항을 추가하지 않음

        // when
        List<AnnouncementDto> result = communicationService.readAnnouncements();

        // then
        assertTrue(result.isEmpty());
    }
}
