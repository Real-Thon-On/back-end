package com.fizz.fizz_server.domain.post.domain;

import com.fizz.fizz_server.domain.challenge.domain.Challenge;
import com.fizz.fizz_server.domain.comment.domain.Comment;
import com.fizz.fizz_server.domain.post.domain.vo.FileType;
import com.fizz.fizz_server.domain.user.domain.RoleType;
import com.fizz.fizz_server.domain.user.domain.User;
import com.fizz.fizz_server.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileType fileType;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private List<String> fileUrls = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @OneToMany(mappedBy = "post" , cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostLike> postLikes = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<View> views = new ArrayList<>();

    @Builder
    public Post(String title, String content, User user, Challenge challenge, FileType fileType, List<String> fileUrls) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.challenge = challenge;
        this.fileType = fileType;
        this.fileUrls = fileUrls;
    }
}
