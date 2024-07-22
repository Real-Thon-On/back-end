package com.fizz.fizz_server.domain.challenge.domain;

import com.fizz.fizz_server.domain.user.domain.User;
import com.fizz.fizz_server.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Participant extends BaseEntity { // 중간테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @Builder
    public Participant( User user, Challenge challenge){
        this.user=user;
        this.challenge=challenge;
    }
}
