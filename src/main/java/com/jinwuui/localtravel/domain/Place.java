package com.jinwuui.localtravel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Double lng;

    private Long rating;

    private String country;

    @Lob
    private List<Double> embedding;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private List<PlaceCategory> placeCategories;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @ManyToOne
    @JoinColumn
    private User user;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Place(String name, String description, Double lat, Double lng, Long rating, String country, User user) {
        this.name = name;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.rating = rating;
        this.country = country;
        this.user = user;
    }

    public void setEmbedding(List<Double> embedding) {
        this.embedding = embedding;
    }

    public void addCategory(Category category) {
        if (this.placeCategories == null) {
            this.placeCategories = new ArrayList<>();
        }

        PlaceCategory placeCategory = PlaceCategory.builder()
                .place(this)
                .category(category)
                .build();

        this.placeCategories.add(placeCategory);
    }

    public void addImage(Image image) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }

        this.images.add(image);
        if (image.getPlace() != this) {
            image.setPlace(this);
        }
    }

    public String getEmbeddingText() {
        final String DELIMITER = " | ";
        StringBuilder embeddingText = new StringBuilder();
        
        embeddingText.append("이름:").append(this.name).append(DELIMITER);
        embeddingText.append("설명:").append(this.description).append(DELIMITER);
        embeddingText.append("latitude:").append(String.format("%.6f", this.lat)).append(DELIMITER);
        embeddingText.append("longitude:").append(String.format("%.6f", this.lng)).append(DELIMITER);
        
        if (this.country != null && !this.country.isEmpty()) {
            embeddingText.append("국가:").append(this.country).append(DELIMITER);
        }
        
        if (this.placeCategories != null && !this.placeCategories.isEmpty()) {
            embeddingText.append("카테고리:");
            for (PlaceCategory placeCategory : this.placeCategories) {
                embeddingText.append(placeCategory.getCategory().getName()).append(",");
            }
            embeddingText.setLength(embeddingText.length() - 1);
            embeddingText.append(DELIMITER);
        }
        
        if (this.rating != null) {
            embeddingText.append("평점:").append(this.rating);
        }
        
        return embeddingText.toString().trim();
    }
}
