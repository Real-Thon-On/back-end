package com.fizz.fizz_server.domain.category.domain;


import com.fizz.fizz_server.domain.challenge.domain.Challenge;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


//    Health("헬스"),
//    Musical_Instruments("악기"),
//    Books("책"),
//    Asceticism("금욕"),
//    Sports("스포츠"),
//    Cooking("요리"),
//    Art("예술"),
//    Technology("기술"),
//    Gardening("원예"),
//    Travel("여행");


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Category {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(nullable = false)
    private String title;


    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Challenge> challenges= new ArrayList<>();

    @Builder
    public Category(String title){
        this.title=title;
    }



}
