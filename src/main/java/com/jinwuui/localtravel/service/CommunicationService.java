package com.jinwuui.localtravel.service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jinwuui.localtravel.domain.Announcement;
import com.jinwuui.localtravel.domain.Feedback;
import com.jinwuui.localtravel.dto.service.AnnouncementDto;
import com.jinwuui.localtravel.dto.service.FeedbackDto;
import com.jinwuui.localtravel.repository.AnnouncementRepository;
import com.jinwuui.localtravel.repository.FeedbackRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommunicationService {

    private final AnnouncementRepository announcementRepository;
    
    private final FeedbackRepository feedbackRepository;

    public List<AnnouncementDto> readAnnouncements() {
        List<Announcement> announcements = announcementRepository.findTop10ByOrderByCreatedAtDesc();

        return announcements.stream()
                .map(announcement -> AnnouncementDto.builder()
                        .id(announcement.getId())
                        .version(announcement.getVersion())
                        .content(announcement.getContent())
                        .createdAt(announcement.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public void createFeedback(FeedbackDto feedbackDto) {
        Feedback feedback = Feedback.builder()
                .writer(feedbackDto.getWriter())
                .content(feedbackDto.getContent())
                .build();
        feedbackRepository.save(feedback);
    }
}
