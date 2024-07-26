package com.fizz.fizz_server.domain.user.repository;

import com.fizz.fizz_server.domain.user.domain.Follow;
import com.fizz.fizz_server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerAndFollowee(User follower, User followee);
    @Query("select f.followee from Follow f where f.follower = :user")
    List<User> findByFollower(@Param("user") User user);
    @Query("select f.follower from Follow f where f.followee = :user")
    List<User> findByFollowee(@Param("user") User user);
}
