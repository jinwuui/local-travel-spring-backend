package com.jinwuui.localtravel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;

import com.jinwuui.localtravel.dto.response.AnnouncementResponse;
import com.jinwuui.localtravel.dto.response.PagingResponse;
import com.jinwuui.localtravel.service.CommunicationService;

import jakarta.validation.Valid;

import com.jinwuui.localtravel.dto.request.FeedbackRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.jinwuui.localtravel.dto.mapper.CommunicationMapper.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/communications")
@RequiredArgsConstructor
public class CommunicationController {
    
    private final CommunicationService communicationService;

    @GetMapping("/announcements")
    public PagingResponse<AnnouncementResponse> getAnnouncements() {
        return toPagingAnnouncementResponse(communicationService.readAnnouncements());
    }
    
    @PostMapping("/feedbacks")
    @ResponseStatus(HttpStatus.CREATED)
    public void postFeedback(@RequestBody @Valid FeedbackRequest feedbackRequest) {
        communicationService.createFeedback(toFeedbackDto(feedbackRequest));
    }
}
