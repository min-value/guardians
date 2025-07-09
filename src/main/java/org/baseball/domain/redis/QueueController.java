package org.baseball.domain.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/queue")
public class QueueController {
    @Autowired
    private QueueService queueService;

    @GetMapping("/enqueue/{gamePk}")
    public String showQueuePage(@PathVariable int gamePk,
                                @RequestParam int userPk,
                                Model model) {
        boolean enqueued = queueService.enqueueUser(gamePk, userPk);
        long pos = queueService.getPosition(gamePk, userPk);
        model.addAttribute("position", pos);
        model.addAttribute("enqueued", enqueued);
        return "/tickets/queueModal"; // JSP 뷰 경로
    }

    @PostMapping("/enqueue/{gamePk}")
    public ResponseEntity<?> joinQueue(@PathVariable int gamePk,
                                       @RequestParam int userPk) {
        boolean enqueued = queueService.enqueueUser(gamePk, userPk);
        if (enqueued) {
            long position = queueService.getPosition(gamePk, userPk);
            return ResponseEntity.ok("대기열에 등록되었습니다. 현재 대기 순번: " + position);
        } else {
            return ResponseEntity.status(400).body("대기열 등록 실패");
        }
    }

    @PostMapping("/try-reserve/{gamePk}")
    public ResponseEntity<?> tryReserve(@PathVariable int gamePk,
                                        @RequestParam int userPk) {
        if (queueService.canReserve(gamePk, userPk)) {
            queueService.pollFront(gamePk);
            return ResponseEntity.ok("예약 가능 상태");
        } else {
            Long pos = queueService.getPosition(gamePk, userPk);
            return ResponseEntity.status(403).body("예약 대기 중입니다. 현재 순번: " + pos);
        }
    }

    @PostMapping("/complete-reservation/{gamePk}")
    public ResponseEntity<?> completeReservation(@PathVariable int gamePk,
                                                 @RequestParam int userPk) {
        boolean removed = queueService.dequeueUser(gamePk, userPk);
        if (removed) {
            return ResponseEntity.ok("");
        } else {
            return ResponseEntity.status(400).body("대기열에서 제거 실패");
        }
    }

    @PostMapping("/leave/{gamePk}")
    @ResponseBody
    public ResponseEntity<?> leaveQueue(@PathVariable int gamePk,
                                        @RequestParam int userPk) {
        boolean removed = queueService.dequeueUser(gamePk, userPk);
        if (removed) {
            log.info("사용자 {} 게임 {} 대기열에서 나감", userPk, gamePk);
            return ResponseEntity.ok("대기열에서 제거됨");
        }
        return ResponseEntity.ok("대기열에 없음");
    }

    @GetMapping("/queue-position/{gamePk}")
    @ResponseBody
    public Long getQueuePosition(@PathVariable int gamePk, @RequestParam int userPk) {
        return queueService.getPosition(gamePk, userPk);
    }

    @GetMapping("/queue-size/{gamePk}")
    @ResponseBody
    public Long getQueueSize(@PathVariable int gamePk) {
        return queueService.getQueueSize(gamePk);
    }

    @GetMapping("/can-reserve/{gamePk}")
    @ResponseBody
    public boolean canReserve(@PathVariable int gamePk, @RequestParam int userPk) {
        return queueService.canReserve(gamePk, userPk);
    }
}
