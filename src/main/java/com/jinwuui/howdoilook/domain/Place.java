package com.jinwuui.howdoilook.domain;

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

    private String hanguls;

    private String chosungs;

    private String alphabets;

    @Lob
    private byte[] embedding;

    @OneToMany(mappedBy = "place")
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
}
