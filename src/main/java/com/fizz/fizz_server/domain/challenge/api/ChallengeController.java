package com.fizz.fizz_server.domain.challenge.api;


import com.fizz.fizz_server.domain.challenge.dto.request.CreateChallengeRequestDto;
import com.fizz.fizz_server.domain.challenge.dto.response.ChallengeInfoResponseDto;
import com.fizz.fizz_server.domain.challenge.dto.response.ChallengeSummaryResponseDto;
import com.fizz.fizz_server.domain.challenge.service.ChallengeService;
import com.fizz.fizz_server.domain.user.domain.User;
import com.fizz.fizz_server.domain.user.repository.UserRepository;
import com.fizz.fizz_server.global.base.response.ResponseBody;
import com.fizz.fizz_server.global.base.response.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;


    // UserPrincipal 추후 추가되면 삭제 예정
    private final UserRepository userRepository;



    //챌린지 생성
    @PostMapping()
    public ResponseEntity<ResponseBody> createChallengeByCategoryId(@Valid @RequestBody CreateChallengeRequestDto requestDto){// parameter 에 @AuthenticationPrincipal UserPrincipal user 추후 추가
        User tempUser = userRepository.findById(1L).get(); // UserPrincipal 추후 추가되면 삭제.

        challengeService.createChallengeByCategoryId( tempUser , requestDto);
        return ResponseEntity.status(HttpStatus.CREATED) .body(ResponseUtil.createSuccessResponse());
    }

    //챌린지 상세정보
    @GetMapping("/info/{challengeId}")
    public ResponseEntity<ResponseBody<ChallengeInfoResponseDto>> getChallengeInfoByChallengeId(@PathVariable Long challengeId ){
        ChallengeInfoResponseDto responseDto = challengeService.getChallengeInfoByChallengeId(challengeId);
        return ResponseEntity.status(HttpStatus.OK) .body(ResponseUtil.createSuccessResponse( responseDto ));
    }

    //모든 잠든 챌린지 목록
    @GetMapping("/sleep")
    public ResponseEntity<ResponseBody<List<ChallengeSummaryResponseDto>>> getSleepingChallengeList(){
        return ResponseEntity.status(HttpStatus.OK) .body(ResponseUtil.createSuccessResponse( null ));
    }


    //모든 활성화 상태 챌린지 목록
    @GetMapping
    public ResponseEntity<ResponseBody<List<ChallengeSummaryResponseDto>>> getActiveChallengeList(){
        return ResponseEntity.status(HttpStatus.OK) .body(ResponseUtil.createSuccessResponse( null ));
    }


    //특정 카테고리의 활성화 상태 챌린지 목록
    @GetMapping("/{categoryId}")
    public ResponseEntity<ResponseBody<List<ChallengeSummaryResponseDto>>> getActiveChallengeListByCategoryId(){
        return ResponseEntity.status(HttpStatus.OK) .body(ResponseUtil.createSuccessResponse( null ));
    }

    //특정 카테고리의 잠든 상태 챌린지 목록
    @GetMapping("/sleep/{categoryId}")
    public ResponseEntity<ResponseBody<List<ChallengeSummaryResponseDto>>> getSleepingChallengeListByCategoryId(){
        return ResponseEntity.status(HttpStatus.OK) .body(ResponseUtil.createSuccessResponse( null ));
    }

    //해당 사용자가 생성한 활성화 상태 챌린지 목록
    @GetMapping("/user")
    public ResponseEntity<ResponseBody<List<ChallengeSummaryResponseDto>>> getActiveChallengeListByUser( ){// parameter 에 @AuthenticationPrincipal UserPrincipal user 추후 추가
        User tempUser = userRepository.findById(1L).get();


        return ResponseEntity.status(HttpStatus.OK) .body(ResponseUtil.createSuccessResponse( null ));
    }


    //해당 사용자가 생성한 활성화 상태 챌린지 목록
    @GetMapping("/sleep/user")
    public ResponseEntity<ResponseBody<List<ChallengeSummaryResponseDto>>> getSleepingChallengeListByUser( ){// parameter 에 @AuthenticationPrincipal UserPrincipal user 추후 추가
        User tempUser = userRepository.findById(1L).get();

        return ResponseEntity.status(HttpStatus.OK) .body(ResponseUtil.createSuccessResponse( null ));
    }

}
