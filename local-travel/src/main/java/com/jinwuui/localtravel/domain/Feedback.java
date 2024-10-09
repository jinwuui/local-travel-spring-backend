package com.jinwuui.localtravel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String writer;

    @Column(nullable = false)
    private String content;

    @Builder
    public Feedback(String writer, String content) {
        this.writer = writer;
        this.content = content;
    }
}
