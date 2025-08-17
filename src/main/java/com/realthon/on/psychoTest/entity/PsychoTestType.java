package com.realthon.on.psychoTest.entity;

import com.realthon.on.community.entity.HashTagType;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PsychoTestType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;          // 검사명
    private String scale;         // 척도
    private int questionCount;    // 문항 수
    private String scoringMethod; // 채점방식

    private int scoreRangeMin;
    private int scoreRangeMax;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<TargetType> targetTypes = new HashSet<>();

    @Column(columnDefinition = "TEXT")
    private String resultMapping; // JSON 문자열

}

