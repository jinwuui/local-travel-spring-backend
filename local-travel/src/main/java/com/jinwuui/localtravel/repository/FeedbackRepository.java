package com.jinwuui.localtravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jinwuui.localtravel.domain.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
