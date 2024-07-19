package com.fizz.fizz_server.domain.challenge.service;
import com.fizz.fizz_server.domain.challenge.dto.request.CreateChallengeRequestDto;
import com.fizz.fizz_server.domain.challenge.dto.response.ChallengeInfoResponseDto;
import com.fizz.fizz_server.domain.challenge.dto.response.ChallengeSummaryResponseDto;
import com.fizz.fizz_server.domain.user.domain.User;
import java.util.List;

public interface ChallengeService {
    //챌린지 생성
    public void createChallengeByCategoryId(User user , CreateChallengeRequestDto requestDto);

    //챌린지 상세정보
    public ChallengeInfoResponseDto getChallengeInfoByChallengeId( Long challengeId );

    //모든 잠든 챌린지 목록
    public List<ChallengeSummaryResponseDto> getSleepingChallengeList();

    //모든 활성화 상태 챌린지 목록
    public List<ChallengeSummaryResponseDto> getActiveChallengeList();

    //특정 카테고리의 활성화 상태 챌린지 목록
    public List<ChallengeSummaryResponseDto> getActiveChallengeListByCategoryId();

    //특정 카테고리의 잠든 상태 챌린지 목록
    public List<ChallengeSummaryResponseDto> getSleepingChallengeListByCategoryId();

    //해당 사용자가 생성한 활성화 상태 챌린지 목록
    public List<ChallengeSummaryResponseDto> getActiveChallengeListByUser( Long userId );

    //해당 사용자가 생성한 활성화 상태 챌린지 목록
    public List<ChallengeSummaryResponseDto> getSleepingChallengeListByUser( Long userId );
}
