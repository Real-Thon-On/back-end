package com.fizz.fizz_server.domain.challenge.scheduler;


import com.fizz.fizz_server.domain.challenge.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChallengeScheduler {

    private final ChallengeService challengeService;

    @Scheduled(cron = "${challenge.schedule.cron}")
    public void changeStateToSleeping(){
        Integer updatedRowCount = challengeService.changeStateToSleeping();
        log.info("changeStateToSleeping method excuted, updated {} rows",updatedRowCount);
    }
}
