package com.realthon.on.emotionDiary.entity;

import com.realthon.on.global.base.domain.BaseEntity;
import com.realthon.on.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EmotionDiary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 500)
    private String content;

    @Enumerated(EnumType.STRING)
    private WeatherType weather;

    private LocalDate date;

    @ElementCollection
    @CollectionTable(name = "diary_hashtag", joinColumns = @JoinColumn(name = "diary_id"))
    @Column(name = "hashtag")
    private Set<String> hashtags = new HashSet<>();

    @Builder
    public EmotionDiary(User user, String content, LocalDate date, WeatherType weather, Set<String> hashtags) {
        this.user = user;

        this.content = content;
        this.date = date != null ? date : LocalDate.now();
        this.weather = weather;
        this.hashtags = hashtags;
    }

    public void update(LocalDate date, WeatherType weather, Set<String>hashtags, String content) {
        this.content = content;
        this.weather = weather;
        this.hashtags = hashtags;
        this.date = date;
    }

}