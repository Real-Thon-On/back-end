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
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    @Column(nullable = false)
    private String content;

    @Builder
    public Comment(User user, Board board,String title, String content) {
        this.user = user;
        this.board = board;
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }
}
