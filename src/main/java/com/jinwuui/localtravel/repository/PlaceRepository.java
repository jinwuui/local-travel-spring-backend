package com.jinwuui.localtravel.repository;

import com.jinwuui.localtravel.domain.Place;

import jakarta.persistence.Tuple;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("SELECT DISTINCT p FROM Place p LEFT JOIN FETCH p.placeCategories pc LEFT JOIN FETCH pc.category")
    List<Place> findAllWithCategories();

    @Query("SELECT DISTINCT p FROM Place p JOIN p.placeCategories pc JOIN pc.category c WHERE c.name = :categoryName")
    List<Place> findByCategoryName(String categoryName);

    @Query("SELECT DISTINCT p FROM Place p LEFT JOIN FETCH p.placeCategories pc LEFT JOIN FETCH pc.category WHERE p.id = :id")
    Optional<Place> findByIdWithCategories(Long id);

    @Query("SELECT DISTINCT p FROM Place p JOIN p.bookmarks b LEFT JOIN FETCH p.images i WHERE b.user.id = :userId")
    List<Place> findBookmarkedPlacesWithImagesByUserId(Long userId);

    @Query(nativeQuery = true, value = "SELECT * FROM place p WHERE (p.embedding <-> cast(:value as vector)) < :similarityThreshold ORDER BY (p.embedding <-> cast(:value as vector)) LIMIT 7")
    List<Place> findSimilarPlaces(String value, double similarityThreshold);
    
}
