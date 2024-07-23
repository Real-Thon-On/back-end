package com.fizz.fizz_server.domain.comment.service;

import com.fizz.fizz_server.domain.comment.domain.Comment;
import com.fizz.fizz_server.domain.comment.domain.CommentLike;
import com.fizz.fizz_server.domain.comment.dto.request.ChangeCommentRequestDto;
import com.fizz.fizz_server.domain.comment.dto.request.CreateChildCommentRequestDto;
import com.fizz.fizz_server.domain.comment.dto.request.CreateParentCommentRequestDto;
import com.fizz.fizz_server.domain.comment.dto.response.CommentInfoResponseDto;
import com.fizz.fizz_server.domain.comment.dto.response.CommentIsLikeResponseDto;
import com.fizz.fizz_server.domain.comment.repository.CommentLikeRepository;
import com.fizz.fizz_server.domain.comment.repository.CommentRepository;
import com.fizz.fizz_server.domain.post.domain.Post;
import com.fizz.fizz_server.domain.post.repository.PostRepository;
import com.fizz.fizz_server.domain.user.domain.User;
import com.fizz.fizz_server.domain.user.repository.UserRepository;
import com.fizz.fizz_server.global.base.response.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.fizz.fizz_server.global.base.response.exception.ExceptionType.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService{
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void createParentCommentByPostId(Long postId, CreateParentCommentRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(()->new BusinessException(POST_NOT_FOUND));
        Comment comment = Comment.builder().
                content(requestDto.getContent()).
                user(user).
                post(post).
                build();
        Comment created = commentRepository.save(comment);
        log.info(created.toString());
    }

    @Transactional
    @Override
    public void createChildCommentByPostId(Long postId, CreateChildCommentRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(()->new BusinessException(POST_NOT_FOUND));
        Comment parentComment = commentRepository.findById(requestDto.getParentCommentId()).orElseThrow(()->new BusinessException(NON_EXISTENT_COMMENT_ERROR));
        Comment comment = Comment.builder().
                content(requestDto.getContent()).
                user(user).
                post(post).
                parent(parentComment).
                build();
        Comment created = commentRepository.save(comment);
        log.info(created.toString());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentInfoResponseDto> getAllParentCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()->new BusinessException(POST_NOT_FOUND));
        List<Comment> entityList= commentRepository.findByPostAndParentIsNull(post);
        List<CommentInfoResponseDto> dtoList = entityListToDtoList(entityList);
        log.info(dtoList.toString());
        return dtoList;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentInfoResponseDto> getAllChildCommentsByCommentId(Long commentId) {
        Comment parentComment = commentRepository.findById(commentId).orElseThrow(()->new BusinessException(NON_EXISTENT_COMMENT_ERROR));
        List<Comment> entityList= commentRepository.findByParent(parentComment);
        List<CommentInfoResponseDto> dtoList = entityListToDtoList(entityList);
        log.info(dtoList.toString());
        return dtoList;
    }

    @Transactional
    @Override
    public void changeCommentByCommentId(Long commentId, ChangeCommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new BusinessException(NON_EXISTENT_COMMENT_ERROR));
        comment.update(requestDto.getContent());
        log.info(comment.toString());
    }

    @Transactional
    @Override
    public void deleteCommentByCommentId(Long commentId) {
        commentRepository.deleteById(commentId);
        log.info("comment {} has been deleted",commentId);
    }

    @Transactional(readOnly = true)
    @Override
    public CommentIsLikeResponseDto getIsLikedByCommentId(Long commentId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new BusinessException(NON_EXISTENT_COMMENT_ERROR));
        boolean isLiked = commentLikeRepository.existsByCommentAndUser(comment, user);
        CommentIsLikeResponseDto responseDto = new CommentIsLikeResponseDto(isLiked);
        log.info(responseDto.toString());
        return responseDto;
    }

    @Transactional
    @Override
    public void createLikeByCommentId(Long commentId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new BusinessException(NON_EXISTENT_COMMENT_ERROR));
        CommentLike commentLike = CommentLike.builder().comment(comment).user(user).build();
        CommentLike created = commentLikeRepository.save(commentLike);
        log.info(created.toString());
    }

    @Transactional
    @Override
    public void deleteLikeByCommentId(Long commentId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new BusinessException(NON_EXISTENT_COMMENT_ERROR));
        commentLikeRepository.deleteByCommentAndUser(comment, user);
        log.info("commentLike {} has been deleted",commentId);
    }

    public List<CommentInfoResponseDto> entityListToDtoList(List<Comment> entityList){
        List<CommentInfoResponseDto> dtoList
                = entityList.stream().map(entity-> CommentInfoResponseDto.toDTO(entity))
                .collect(Collectors.toList());
        return dtoList;
    }
}
