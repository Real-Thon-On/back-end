package com.fizz.fizz_server.domain.user.domain;

import com.fizz.fizz_server.domain.challenge.domain.Challenge;
import com.fizz.fizz_server.domain.challenge.domain.Participant;
import com.fizz.fizz_server.domain.comment.domain.Comment;
import com.fizz.fizz_server.domain.comment.domain.CommentLike;
import com.fizz.fizz_server.domain.post.domain.Post;
import com.fizz.fizz_server.domain.post.domain.PostLike;
import com.fizz.fizz_server.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String nickname;

    private String profileId;
    private String profileImage;
    private String email;
    private String aboutMe; // 자기소개

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> followers = new HashSet<>();

    @OneToMany(mappedBy = "followee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> followees = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "creator")
    private List<Challenge> createdChallenges = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private Set<PostLike> postLikes = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<CommentLike> commentLikes = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<Participant> participants = new ArrayList<>();

    @Builder
    public User(String nickname, String profileId, String profileImage, String email, String aboutMe, RoleType role) {
        this.nickname = nickname;
        this.profileId = profileId;
        this.profileImage = profileImage;
        this.email = email;
        this.aboutMe = aboutMe;
        this.role = role;
    }
}
