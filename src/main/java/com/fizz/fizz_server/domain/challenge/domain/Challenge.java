package com.fizz.fizz_server.domain.challenge.domain;

import com.fizz.fizz_server.domain.post.domain.Post;
import com.fizz.fizz_server.domain.user.domain.User;
import com.fizz.fizz_server.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Challenge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @OneToMany(mappedBy = "challenge")
    private List<Post> posts = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @OneToMany(mappedBy = "challenge")
    private List<Participant> participants = new ArrayList<>();

    @Builder
    public Challenge(User creator, Category category, String description, String title) {
        this.creator = creator;
        this.category = category;
        this.description = description;
        this.title = title;
    }
}
