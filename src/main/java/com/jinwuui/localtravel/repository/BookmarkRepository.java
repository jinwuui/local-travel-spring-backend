package com.jinwuui.localtravel.repository;

import com.jinwuui.localtravel.domain.Bookmark;
import com.jinwuui.localtravel.domain.Place;
import com.jinwuui.localtravel.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByUserIdAndPlaceId(Long userId, Long placeId);

    void deleteByUserAndPlace(User user, Place place);

}
