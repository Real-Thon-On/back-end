package com.fizz.fizz_server.domain.example.user.domain;


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
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Setter
    private String nickname;

    @Setter
    @Column(unique = true)
    private String email;


    private String providerId;

    @Setter
    @Column(unique = true)
    private String profileId;

    @Setter
    private String profileImage;

    @Setter
    private String aboutMe; // 자기소개

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> followers = new HashSet<>();

    @OneToMany(mappedBy = "followee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> followees = new HashSet<>();



    @Builder
    public User(String providerId, String nickname, String profileId, String profileImage, String email, String aboutMe, RoleType role) {
        this.providerId = providerId;
        this.nickname = nickname;
        this.profileId = profileId;
        this.profileImage = profileImage;
        this.email = email;
        this.aboutMe = aboutMe;
        this.role = role;
    }
}
