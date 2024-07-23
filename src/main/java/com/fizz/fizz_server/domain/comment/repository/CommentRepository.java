package com.fizz.fizz_server.domain.comment.repository;

import com.fizz.fizz_server.domain.comment.domain.Comment;
import com.fizz.fizz_server.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostAndParentIsNull(Post post);
    List<Comment> findByParent(Comment parent);

}
