package com.fizz.fizz_server.domain.file.domain;

import com.fizz.fizz_server.domain.post.domain.Post;
import com.fizz.fizz_server.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class File extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @Column(nullable = false)
    private String origin_name;

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name ="post_id")
    private Post post;
}
