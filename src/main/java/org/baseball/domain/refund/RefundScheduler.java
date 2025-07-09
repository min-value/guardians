package org.baseball.domain.refund;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefundScheduler {

    private final RefundService refundService;

    @Scheduled(cron = "0 55 00 * * *")
    public void processRefundForCanceledGames() {
        log.info("취소된 경기 자동 환불 시작");

        try {
            int refundCount = refundService.refundCanceledGames();
            log.info("환불 완료된 건수: {}", refundCount);
        } catch (Exception e) {
            log.error("환불 처리 중 예외 발생", e);
        }
    }
}