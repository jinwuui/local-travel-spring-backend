package com.jinwuui.localtravel.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.jinwuui.localtravel.dto.response.AnnouncementResponse;
import com.jinwuui.localtravel.dto.service.AnnouncementDto;
import com.jinwuui.localtravel.dto.response.PagingResponse;

public class CommunicationMapper {
    
    public static PagingResponse<AnnouncementResponse> toPagingAnnouncementResponse(List<AnnouncementDto> announcementDtos) {
        List<AnnouncementResponse> responses = announcementDtos.stream()
                .map(dto -> AnnouncementResponse.builder()
                        .id(dto.getId())
                        .version(dto.getVersion())
                        .content(dto.getContent())
                        .createdAt(dto.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return PagingResponse.<AnnouncementResponse>builder()
                .size(responses.size())
                .items(responses)
                .build();
    }
}
