package com.fizz.fizz_server.domain.comment.repository;

import com.fizz.fizz_server.domain.comment.dto.response.CommentInfoWithChildCountResponseDto;
import com.fizz.fizz_server.domain.post.domain.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CommentInfoWithChildCountResponseDto> findParentCommentsWithChildCountByPost(Post post) {
        String jpql = "SELECT new com.fizz.fizz_server.domain.comment.dto.response.CommentInfoWithChildCountResponseDto(" +
                "c.id, " +
                "c.parent.id, " +
                "c.user.id, " +
                "c.content, " +
                "SIZE(c.commentLikes), " +
                "COUNT(child), " +
                "c.user.nickname, " +
                "c.createdAt) " +
                "FROM Comment c LEFT JOIN c.children child " +
                "WHERE c.post = :post AND c.parent IS NULL " +
                "GROUP BY c";
        TypedQuery<CommentInfoWithChildCountResponseDto> query = entityManager.createQuery(jpql, CommentInfoWithChildCountResponseDto.class);
        query.setParameter("post", post);
        return query.getResultList();
    }
}
