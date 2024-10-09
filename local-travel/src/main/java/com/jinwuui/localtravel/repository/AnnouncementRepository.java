package com.jinwuui.localtravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.jinwuui.localtravel.domain.Announcement;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    List<Announcement> findTop10ByOrderByCreatedAtDesc();

}
