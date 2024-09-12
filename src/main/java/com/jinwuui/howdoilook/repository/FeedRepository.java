package com.jinwuui.howdoilook.repository;

import com.jinwuui.howdoilook.domain.Feed;
import com.jinwuui.howdoilook.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {
}
