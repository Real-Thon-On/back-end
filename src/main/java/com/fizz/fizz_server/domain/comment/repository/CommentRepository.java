package com.fizz.fizz_server.domain.comment.repository;

import com.fizz.fizz_server.domain.comment.domain.Comment;
import com.fizz.fizz_server.domain.comment.dto.response.CommentDetailResponseDto;
import com.fizz.fizz_server.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostAndParentIsNull(Post post);
    List<Comment> findByParent(Comment parent);
    @Query("SELECT new com.fizz.fizz_server.domain.comment.dto.response.CommentDetailResponseDto(" +
            "c.id, " +
            "c.parent.id, " +
            "c.user.id, " +
            "c.content, " +
            "SIZE(c.commentLikes), " +
            "COUNT(child), " +
            "c.user.nickname, " +
            "c.createdAt, " +
            "c.user.profileImage) " +
            "FROM Comment c LEFT JOIN c.children child " +
            "WHERE c.post = :post AND c.parent IS NULL " +
            "GROUP BY c.id, c.parent.id, c.user.id, c.content, c.createdAt, c.user.nickname, c.user.profileImage")
    List<CommentDetailResponseDto> findParentCommentsWithChildCountByPost(@Param("post") Post post);

    @Query("SELECT new com.fizz.fizz_server.domain.comment.dto.response.CommentDetailResponseDto(" +
            "c.id, " +
            "c.parent.id, " +
            "c.user.id, " +
            "c.content, " +
            "SIZE(c.commentLikes), " +
            "0L, " + // 자식 댓글의 childCount는 0으로 설정
            "u.nickname, " +
            "c.createdAt, " +
            "u.profileImage) " +
            "FROM Comment c " +
            "JOIN c.user u " +
            "WHERE c.parent = :parentComment " +
            "GROUP BY c.id, c.parent.id, c.user.id, c.content, c.createdAt, u.nickname, u.profileImage")
    List<CommentDetailResponseDto> findChildCommentsByParent(@Param("parentComment") Comment parentComment);





}
