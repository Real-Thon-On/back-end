package com.fizz.fizz_server.domain.category.repository;

import com.fizz.fizz_server.domain.category.domain.CategoryRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRecommendationRepository extends JpaRepository<CategoryRecommendation, Long> {
}

