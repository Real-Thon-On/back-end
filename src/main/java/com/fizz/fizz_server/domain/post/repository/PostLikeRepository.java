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

import java.net.ContentHandler;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByPostAndUser(Post post, User user);

    Optional<PostLike> findByPostAndUser(Post post, User user);

    @Query("SELECT pl.post FROM PostLike pl WHERE pl.user.id = :userId")
    Page<Post> findPostsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT pl.post.id FROM PostLike pl WHERE pl.user.id = :userId")
    Page<Long> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT DISTINCT pl.user.id FROM PostLike pl GROUP BY pl.user HAVING COUNT(pl.post) >= :minLikes")
    List<Long> findUsersWithMinLikes(@Param("minLikes") int minLikes);

    @Query("SELECT DISTINCT pl.post.id FROM PostLike pl WHERE pl.user.id = :user")
    Set<Long> findPostsLikedByUsers(@Param("user") Long user);
}
