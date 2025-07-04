package org.baseball.domain.point;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointScheduler {

    private static final Logger log = LoggerFactory.getLogger(PointScheduler.class);
    private final PointService pointService;

    @Scheduled(cron = "0 0 2 * * *")
    public void schedulePointEarn() {
        log.info("스케줄러 실행");
        pointService.earnPredictionPoints();
        log.info("실행 완료");
    }
}