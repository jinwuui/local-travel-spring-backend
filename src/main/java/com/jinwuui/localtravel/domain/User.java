package com.jinwuui.localtravel.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "이메일은 필수 입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 입니다.")
    private String nickname;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarks;

    @Builder
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.createdAt = LocalDateTime.now();
    }

    public void addBookmark(Place place) {
        if (this.bookmarks == null) {
            this.bookmarks = new ArrayList<>();
        }

        if (this.bookmarks.stream().noneMatch(b -> b.getPlace().equals(place))) {
            Bookmark bookmark = Bookmark.builder()
                    .user(this)
                    .place(place)
                    .build();

            this.bookmarks.add(bookmark);
            place.getBookmarks().add(bookmark);
        }
    }
}
