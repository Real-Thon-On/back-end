package com.fizz.fizz_server.domain.post.service;

import com.fizz.fizz_server.domain.challenge.domain.Challenge;
import com.fizz.fizz_server.domain.challenge.domain.Participant;
import com.fizz.fizz_server.domain.challenge.repository.ChallengeRepository;
import com.fizz.fizz_server.domain.challenge.repository.ParticipantRepository;
import com.fizz.fizz_server.domain.post.domain.Post;
import com.fizz.fizz_server.domain.post.domain.View;
import com.fizz.fizz_server.domain.post.domain.vo.FileType;
import com.fizz.fizz_server.domain.post.dto.request.PostRequest;
import com.fizz.fizz_server.domain.post.dto.response.PostInfo;
import com.fizz.fizz_server.domain.post.repository.PostLikeRepository;
import com.fizz.fizz_server.domain.post.repository.PostRepository;
import com.fizz.fizz_server.domain.post.repository.ViewRepository;
import com.fizz.fizz_server.domain.user.domain.CustomUserPrincipal;
import com.fizz.fizz_server.domain.user.domain.User;
import com.fizz.fizz_server.domain.user.repository.UserRepository;
import com.fizz.fizz_server.global.base.response.exception.BusinessException;
import com.fizz.fizz_server.global.base.response.exception.ExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.fizz.fizz_server.global.base.response.exception.ExceptionType.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final ViewRepository viewRepository;
    private final ParticipantRepository participantRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public void upload(Long challengeId, PostRequest request, Long userId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new BusinessException(NON_EXISTENT_CHALLENGE_ERROR));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        List<String> fileUrls = extractFileUrls(request);
        FileType fileType = determineFileType(request);

        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .challenge(challenge)
                .user(user)
                .fileType(fileType)
                .fileUrls(fileUrls)
                .build();

        postRepository.save(post);

        Participant participant = Participant.builder()
                .user(user)
                .challenge(challenge)
                .build();

        participantRepository.save(participant);

    }

    private List<String> extractFileUrls(PostRequest request) {
        List<String> fileUrls = new ArrayList<>();
        if (request.images() != null && !request.images().isEmpty()) {
            fileUrls.addAll(request.images());
        } else if (request.video() != null && !request.video().isEmpty()) {
            fileUrls.addAll(request.video());
        }
        return fileUrls;
    }

    private FileType determineFileType(PostRequest request) {
        if (request.images() != null && !request.images().isEmpty()) {
            return FileType.IMAGE;
        } else if (request.video() != null && !request.video().isEmpty()) {
            return FileType.VIDEO;
        }
        throw new BusinessException(FILE_NOT_FOUND);
    }

    @Transactional
    public PostInfo getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ExceptionType.POST_NOT_FOUND));

        // 최대한 간단하게 구현한 조회수 로직. 성능상 문제가 있을 것 같긴한데, 구현이 급하기에 일단..
        View view = new View(post);
        viewRepository.save(view);

        return PostInfo.from(post);
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ExceptionType.POST_NOT_FOUND));

        if(!post.getUser().getId().equals(userId)) {
            throw new BusinessException(ExceptionType.POST_USER_NOT_MATCHED);
        }

        List<Participant> participants = participantRepository.findByUserAndChallenge(post.getUser(), post.getChallenge());
        participantRepository.deleteAll(participants);
        
        postRepository.delete(post);
    }

    public Page<PostInfo> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(PostInfo::from);
    }

    public Page<PostInfo> getPostsByChallenge(Long challengeId, Pageable pageable) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new BusinessException(NON_EXISTENT_CHALLENGE_ERROR));

        return postRepository.findByChallenge(challenge, pageable)
                .map(PostInfo::from);
    }

    public Page<PostInfo> getPostsByUser(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        return postRepository.findByUser(user, pageable)
                .map(PostInfo::from);
    }

    public Page<PostInfo> searchPosts(String keyword, Pageable pageable) {
        return postRepository.searchByKeyword(keyword, pageable)
                .map(PostInfo::from);
    }


    public Page<PostInfo> getPostsByUserLike(Long userId, Pageable pageable) {
        return postLikeRepository.findPostsByUserId(userId, pageable)
                .map(PostInfo::from);
    }
}
