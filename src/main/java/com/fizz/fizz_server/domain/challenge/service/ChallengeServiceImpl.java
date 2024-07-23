package com.fizz.fizz_server.domain.challenge.service;

import com.fizz.fizz_server.domain.category.domain.Category;
import com.fizz.fizz_server.domain.category.repository.CategoryRepository;
import com.fizz.fizz_server.domain.challenge.domain.Challenge;
import com.fizz.fizz_server.domain.challenge.domain.Participant;
import com.fizz.fizz_server.domain.challenge.dto.request.CreateChallengeRequestDto;
import com.fizz.fizz_server.domain.challenge.dto.response.ChallengeInfoResponseDto;
import com.fizz.fizz_server.domain.challenge.dto.response.ChallengeSummaryResponseDto;
import com.fizz.fizz_server.domain.challenge.repository.ChallengeRepository;
import com.fizz.fizz_server.domain.challenge.repository.ParticipantRepository;
import com.fizz.fizz_server.domain.user.domain.User;
import com.fizz.fizz_server.global.base.response.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.fizz.fizz_server.global.base.response.exception.ExceptionType.NON_EXISTENT_CATEGORY_ERROR;
import static com.fizz.fizz_server.global.base.response.exception.ExceptionType.NON_EXISTENT_CHALLENGE_ERROR;


@Slf4j
@RequiredArgsConstructor
@Service
public class ChallengeServiceImpl implements ChallengeService{
   private final ChallengeRepository challengeRepository;
   private final CategoryRepository categoryRepository;
   private final ParticipantRepository participantRepository;

    @Value("${challenge.period.months}")
    private int period;

    @Transactional
    @Override
    public void createChallengeByCategoryId(User user , CreateChallengeRequestDto requestDto) {
        Category category = categoryRepository.findById(requestDto.getCategoryId()).orElseThrow(()->new BusinessException(NON_EXISTENT_CATEGORY_ERROR));
        Challenge challenge = requestDto.toChallenge(user, category);
        Challenge savedChallenge = challengeRepository.save(challenge);
        log.info(savedChallenge.toString());

        Participant participant = Participant.builder().user(user).challenge(savedChallenge).build();
        Participant savedParticipant = participantRepository.save(participant);
        log.info(participant.toString());
    }

    @Transactional(readOnly = true)
    @Override
    public ChallengeInfoResponseDto getChallengeInfoByChallengeId(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(()->new BusinessException(NON_EXISTENT_CHALLENGE_ERROR));
        Integer participantCounts = participantRepository.countByChallenge(challenge);
        ChallengeInfoResponseDto responseDto = ChallengeInfoResponseDto.toDTO(challenge,participantCounts);
        log.info(responseDto.toString());
        return responseDto;
    }

    @Transactional(readOnly = true)
    @Override
    public ChallengeInfoResponseDto getChallengeInfoByChallengeTitle(String challengeTitle) {
        Challenge challenge = challengeRepository.findByTitle(challengeTitle).orElseThrow(()->new BusinessException(NON_EXISTENT_CHALLENGE_ERROR));
        Integer participantCounts = participantRepository.countByChallenge(challenge);
        ChallengeInfoResponseDto responseDto = ChallengeInfoResponseDto.toDTO(challenge,participantCounts);
        log.info(responseDto.toString());
        return responseDto;
    }

    @Transactional
    @Override
    public Integer changeStateToSleeping() {
        LocalDateTime period = LocalDateTime.now().minusMonths(this.period);
        Integer updated = challengeRepository.changeStateToSleeping(period);
        return updated;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChallengeSummaryResponseDto> getSleepingChallengeList()  {
        List<Challenge> entityList = challengeRepository.findByIsActiveFalse();
        List<ChallengeSummaryResponseDto> dtoList = entityListToDtoList(entityList);
        log.info(dtoList.toString());
        return dtoList;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChallengeSummaryResponseDto> getActiveChallengeList()  {
        List<Challenge> entityList = challengeRepository.findByIsActiveTrue();
        List<ChallengeSummaryResponseDto> dtoList = entityListToDtoList(entityList);
        log.info(dtoList.toString());
        return dtoList;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChallengeSummaryResponseDto> getSleepingChallengeListByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new BusinessException(NON_EXISTENT_CATEGORY_ERROR));
        List<Challenge> entityList = challengeRepository.findByCategoryAndIsActiveFalse(category);
        List<ChallengeSummaryResponseDto> dtoList = entityListToDtoList(entityList);
        log.info(dtoList.toString());
        return dtoList;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChallengeSummaryResponseDto> getActiveChallengeListByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new BusinessException(NON_EXISTENT_CATEGORY_ERROR));
        List<Challenge> entityList = challengeRepository.findByCategoryAndIsActiveTrue(category);
        List<ChallengeSummaryResponseDto> dtoList = entityListToDtoList(entityList);
        log.info(dtoList.toString());
        return dtoList;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChallengeSummaryResponseDto> getSleepingChallengeListByUser( User user ) {
        List<Challenge> entityList = challengeRepository.findByCreatorAndIsActiveFalse(user);
        List<ChallengeSummaryResponseDto> dtoList = entityListToDtoList(entityList);
        log.info(dtoList.toString());
        return dtoList;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChallengeSummaryResponseDto> getActiveChallengeListByUser( User user ) {
        List<Challenge> entityList = challengeRepository.findByCreatorAndIsActiveTrue(user);
        List<ChallengeSummaryResponseDto> dtoList = entityListToDtoList(entityList);
        log.info(dtoList.toString());
        return dtoList;
    }

    public List<ChallengeSummaryResponseDto> entityListToDtoList(List<Challenge> entityList){
        List<ChallengeSummaryResponseDto> dtoList
                = entityList.stream().
                map(entity-> ChallengeSummaryResponseDto.toDTO(entity, participantRepository.countByChallenge(entity)))
                .collect(Collectors.toList());
        return dtoList;
    }

}
