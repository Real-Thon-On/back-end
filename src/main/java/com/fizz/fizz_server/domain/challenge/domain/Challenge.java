package com.fizz.fizz_server.domain.challenge.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fizz.fizz_server.domain.category.domain.Category;
import com.fizz.fizz_server.domain.post.domain.Post;
import com.fizz.fizz_server.domain.user.domain.User;
import com.fizz.fizz_server.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Challenge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String description;

    @JoinColumn(name = "category_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @OneToMany(mappedBy = "challenge")
    private List<Post> posts = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @OneToMany(mappedBy = "challenge")
    private List<Participant> participants = new ArrayList<>();

    @Column(nullable = false)
    private boolean isActive;

    @Builder
    public Challenge(User creator, Category category, String description, boolean isActive,String title) {
        this.creator = creator;
        this.category = category;
        this.description = description;
        this.isActive=isActive;
        this.title = title;
    }
}
