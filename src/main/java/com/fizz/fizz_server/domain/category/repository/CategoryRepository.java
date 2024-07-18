package com.fizz.fizz_server.domain.category.repository;

import com.fizz.fizz_server.domain.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}

