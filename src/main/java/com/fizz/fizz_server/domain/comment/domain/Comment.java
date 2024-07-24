package com.fizz.fizz_server.domain.comment.domain;

import com.fizz.fizz_server.domain.post.domain.Post;
import com.fizz.fizz_server.domain.user.domain.User;
import com.fizz.fizz_server.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ToString(exclude = {"parent", "children", "commentLikes"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parent;

    @OneToMany(fetch = FetchType.LAZY ,mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Comment> children = new ArrayList<>();

    @OneToMany(mappedBy = "comment")
    private Set<CommentLike> commentLikes = new HashSet<>();

    @Builder
    public Comment(String content, User user, Post post, Comment parent) {
        this.content = content;
        this.user = user;
        this.post = post;
        this.parent = parent;
    }

    public void update(String content){
        this.content = content;
    }


}
