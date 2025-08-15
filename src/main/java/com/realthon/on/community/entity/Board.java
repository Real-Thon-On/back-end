package com.realthon.on.community.entity;

import com.realthon.on.global.base.domain.BaseEntity;
import com.realthon.on.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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



    @ElementCollection(fetch = FetchType.EAGER,targetClass = HashTagType.class)
    @CollectionTable(name = "board_hashtags", joinColumns = @JoinColumn(name = "board_id"))
    @Enumerated(EnumType.STRING)
    private Set<HashTagType> hashtags = new HashSet<>();

    @Builder
    public Board(User user, String title, String content, Set<HashTagType> hashtags) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.hashtags = hashtags != null ? hashtags : new HashSet<>();
    }

    public void update(String title, String content,Set<HashTagType> hashtags) {
        this.title = title;
        this.content = content;
        this.hashtags.clear();
        if (hashtags != null) {
            this.hashtags.addAll(hashtags);
        }
    }
}
