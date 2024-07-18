package com.fizz.fizz_server.domain.category.domain;

import com.fizz.fizz_server.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;





@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "category_recommendation")
public class CategoryRecommendation {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_recommendation_id")
    private Long categoryRecommendationId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Builder
    public CategoryRecommendation(String title,String description, User user ){
        this.title=title;
        this.description = description;
        this.user= user;
    }



}
