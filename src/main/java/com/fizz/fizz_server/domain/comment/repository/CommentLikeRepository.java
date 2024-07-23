package com.fizz.fizz_server.domain.comment.repository;

import com.fizz.fizz_server.domain.comment.domain.Comment;
import com.fizz.fizz_server.domain.comment.domain.CommentLike;
import com.fizz.fizz_server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    boolean existsByCommentAndUser(Comment comment, User user);

    void deleteByCommentAndUser(Comment comment, User user);


}
