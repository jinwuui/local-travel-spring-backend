package com.jinwuui.howdoilook.repository;

import com.jinwuui.howdoilook.domain.Image;
import com.jinwuui.howdoilook.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
