package com.fizz.fizz_server.domain.post.repository;

import com.fizz.fizz_server.domain.challenge.domain.Challenge;
import com.fizz.fizz_server.domain.post.domain.Post;
import com.fizz.fizz_server.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {


    Page<Post> findByChallenge(Challenge challenge, Pageable pageable);

    Page<Post> findByUser(User user, Pageable pageable);

    /**
     * FULLTEXT 인덱스 적용?
     * 검색 결과 이후 추가 게시물?
     */
    @Query("SELECT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Post> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p.id FROM Post p WHERE p.user.id = :userId")
    List<Long> findPostIdByUserId(@Param("userId") Long userId);

}
