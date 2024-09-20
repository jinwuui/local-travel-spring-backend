package com.jinwuui.localtravel.repository;

import com.jinwuui.localtravel.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
