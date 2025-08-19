package com.realthon.on.community.entity;

import com.realthon.on.global.base.domain.BaseEntity;
import com.realthon.on.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private HashTagType boardType;

    private List<String> hashtags = new ArrayList<>();

    @Builder
    public Board(User user, String title, String content, HashTagType boardType, List<String> hashtags) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.boardType = boardType;
        this.hashtags = hashtags != null ? hashtags : new ArrayList<>();

    }

    public void update(String title, String content,HashTagType boardType, List<String> hashtags) {
        this.title = title;
        this.content = content;
        this.boardType=boardType;

        if (this.hashtags == null) {
            this.hashtags = new ArrayList<>();
        } else {
            this.hashtags.clear();
        }
        if (hashtags != null) {
            this.hashtags.addAll(hashtags);
        }
    }
}
