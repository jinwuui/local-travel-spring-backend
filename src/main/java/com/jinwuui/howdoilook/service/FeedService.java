package com.jinwuui.howdoilook.service;

import com.jinwuui.howdoilook.domain.Feed;
import com.jinwuui.howdoilook.domain.User;
import com.jinwuui.howdoilook.exception.UserNotFoundException;
import com.jinwuui.howdoilook.repository.FeedRepository;
import com.jinwuui.howdoilook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;

    private final UserRepository userRepository;

    public Feed create(Long userId, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Feed feed = Feed.builder()
                .content(content)
                .user(user)
                .build();
        return feedRepository.save(feed);
    }
}
