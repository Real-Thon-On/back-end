package com.fizz.fizz_server.domain.post.repository;

import com.fizz.fizz_server.domain.post.domain.Post;
import com.fizz.fizz_server.domain.post.domain.PostLike;
import com.fizz.fizz_server.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByPostAndUser(Post post, User user);

    Optional<PostLike> findByPostAndUser(Post post, User user);

    @Query("SELECT pl.post FROM PostLike pl WHERE pl.user.id = :userId")
    Page<Post> findPostsByUserId(@Param("userId") Long userId, Pageable pageable);
}
