package com.jinwuui.howdoilook.repository;

import com.jinwuui.howdoilook.domain.Place;
import com.jinwuui.howdoilook.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
