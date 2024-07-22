package com.fizz.fizz_server.domain.challenge.repository;

import com.fizz.fizz_server.domain.category.domain.Category;
import com.fizz.fizz_server.domain.challenge.domain.Challenge;
import com.fizz.fizz_server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    Optional<Challenge> findByTitle(String title);

    @Modifying
    @Query("UPDATE Challenge c SET c.isActive = false WHERE c.isActive = true AND c.createdAt <= :period")
    Integer changeStateToSleeping(LocalDateTime period);

    List<Challenge> findByIsActiveTrue();

    List<Challenge> findByIsActiveFalse();

    @Query("SELECT c FROM Challenge c WHERE c.category = :category AND c.isActive = true")
    List<Challenge> findByCategoryAndIsActiveTrue(Category category);

    @Query("SELECT c FROM Challenge c WHERE c.category = :category AND c.isActive = false")
    List<Challenge> findByCategoryAndIsActiveFalse(Category category);

    @Query("SELECT c FROM Challenge c WHERE c.creator = :user AND c.isActive = true")
    List<Challenge> findByCreatorAndIsActiveTrue(User user);

    @Query("SELECT c FROM Challenge c WHERE c.creator = :user AND c.isActive = false")
    List<Challenge> findByCreatorAndIsActiveFalse(User user);

}
