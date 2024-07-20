package com.fizz.fizz_server.domain.file.domain;

import com.fizz.fizz_server.domain.post.domain.Post;
import com.fizz.fizz_server.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class File extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;
    private String mediaType;
    private String fileFormat;
    private String fileName;
    private String uuid;
    private Long size;
    @Setter
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="post_id")
    private Post post;

    @Builder
    public File(String mediaType, String fileFormat, String fileName, String uuid, Long size, Post post) {
        this.mediaType = mediaType;
        this.fileFormat = fileFormat;
        this.fileName = fileName;
        this.uuid = uuid;
        this.size = size;
        this.post = post;
    }
}
