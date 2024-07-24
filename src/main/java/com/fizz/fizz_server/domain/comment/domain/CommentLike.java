package com.fizz.fizz_server.domain.comment.domain;

import com.fizz.fizz_server.domain.user.domain.User;
import com.fizz.fizz_server.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment_like", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"comment_id", "user_id"})
})
@Entity
@EqualsAndHashCode(of = {"comment", "user"}, callSuper = false)
public class CommentLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public CommentLike(Comment comment, User user){
        this.comment = comment;
        this.user = user;
    }
}
