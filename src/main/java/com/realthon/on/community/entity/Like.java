package com.realthon.on.community.entity;

import com.realthon.on.global.base.domain.BaseEntity;
import com.realthon.on.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "board_like",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"board_id", "user_id"})})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Like(User user, Board board) {
        this.user = user;
        this.board = board;
    }
}
