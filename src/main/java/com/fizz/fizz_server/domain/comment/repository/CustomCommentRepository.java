package com.fizz.fizz_server.domain.comment.repository;


import com.fizz.fizz_server.domain.comment.dto.response.CommentInfoWithChildCountResponseDto;
import com.fizz.fizz_server.domain.post.domain.Post;

import java.util.List;

public interface CustomCommentRepository {
    List<CommentInfoWithChildCountResponseDto> findParentCommentsWithChildCountByPost(Post post);
}
