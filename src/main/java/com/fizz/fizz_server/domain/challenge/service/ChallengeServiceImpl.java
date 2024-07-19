package com.fizz.fizz_server.domain.challenge.service;

import com.fizz.fizz_server.domain.category.domain.Category;
import com.fizz.fizz_server.domain.category.repository.CategoryRepository;
import com.fizz.fizz_server.domain.challenge.domain.Challenge;
import com.fizz.fizz_server.domain.challenge.dto.request.CreateChallengeRequestDto;
import com.fizz.fizz_server.domain.challenge.dto.response.ChallengeInfoResponseDto;
import com.fizz.fizz_server.domain.challenge.dto.response.ChallengeSummaryResponseDto;
import com.fizz.fizz_server.domain.challenge.repository.ChallengeRepository;
import com.fizz.fizz_server.domain.user.domain.User;
import com.fizz.fizz_server.global.base.response.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.fizz.fizz_server.global.base.response.exception.ExceptionType.NON_EXISTENT_CATEGORY_ERROR;
import static com.fizz.fizz_server.global.base.response.exception.ExceptionType.NON_EXISTENT_CHALLENGE_ERROR;


@Slf4j
@RequiredArgsConstructor
@Service
public class ChallengeServiceImpl implements ChallengeService{

   private final ChallengeRepository challengeRepository;
   private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public void createChallengeByCategoryId(User user , CreateChallengeRequestDto requestDto) {
        Category category = categoryRepository.findById(requestDto.getCategoryId()).orElseThrow(()->new BusinessException(NON_EXISTENT_CATEGORY_ERROR));
        Challenge challenge = requestDto.toChallenge(user, category);
        Challenge savedChallenge = challengeRepository.save(challenge);
        log.info(savedChallenge.toString());
    }

    @Override
    public ChallengeInfoResponseDto getChallengeInfoByChallengeId(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(()->new BusinessException(NON_EXISTENT_CHALLENGE_ERROR));
        ChallengeInfoResponseDto responseDto = ChallengeInfoResponseDto.EntityToDTO(challenge);
        log.info(responseDto.toString());
        return responseDto;
    }

    @Override
    public List<ChallengeSummaryResponseDto> getSleepingChallengeList()  {
        return List.of();
    }

    @Override
    public List<ChallengeSummaryResponseDto> getActiveChallengeList()  {
        return List.of();
    }
    @Override
    public List<ChallengeSummaryResponseDto> getActiveChallengeListByCategoryId() {
        return List.of();
    }

    @Override
    public List<ChallengeSummaryResponseDto> getActiveChallengeListByUser(Long userId) {
        return List.of();
    }

    @Override
    public List<ChallengeSummaryResponseDto> getSleepingChallengeListByCategoryId() {
        return List.of();
    }

    @Override
    public List<ChallengeSummaryResponseDto> getSleepingChallengeListByUser(Long userId) {
        return List.of();
    }



}
