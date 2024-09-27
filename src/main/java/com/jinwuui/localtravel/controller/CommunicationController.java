package com.jinwuui.localtravel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jinwuui.localtravel.dto.response.AnnouncementResponse;
import com.jinwuui.localtravel.dto.response.PagingResponse;
import com.jinwuui.localtravel.service.CommunicationService;

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
}
