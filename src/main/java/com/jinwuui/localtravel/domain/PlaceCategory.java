package com.jinwuui.localtravel.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PlaceCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn()
    private Place place;

    @ManyToOne
    @JoinColumn()
    private Category category;

    @Builder
    public PlaceCategory(Place place, Category category) {
        this.place = place;
        this.category = category;
    }
}
