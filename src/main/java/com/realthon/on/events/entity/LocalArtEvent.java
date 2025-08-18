package com.realthon.on.events.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "local_art_event")
public class LocalArtEvent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String url;

    private LocalDate startDate;
    private LocalDate endDate;

    private String imageUrl;
    private String categories;
    private String mainCategory;
    private String siteName;
    private String siteType;

    private LocalDate scrapedDate;
    private String detailTitle;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT") // 또는 "LONGTEXT"
    private String detailImages;
    private Integer imageCount;
}
