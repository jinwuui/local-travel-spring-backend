package com.jinwuui.howdoilook.service;

import com.jinwuui.howdoilook.domain.Feed;
import com.jinwuui.howdoilook.dto.service.FeedDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedCreationService {

    private final FeedService feedService;

    private final ImageService imageService;

    @Transactional
    public Long createFeedWithImages(Long userId, FeedDto feedDto) {
        Feed feed  = feedService.create(userId, feedDto.getContent());

        imageService.saveImages(feed, feedDto.getImages());

        return feed.getId();
    }
}
