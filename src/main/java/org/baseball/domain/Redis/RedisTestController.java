package org.baseball.domain.Redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
public class RedisTestController {
    @Autowired
    private RedisTestService redisTestService;

    @Autowired
    private RedissonTestService redissonTestService;

    @Autowired
    RedisService redisService;

    @GetMapping("/redis-test")
    public String test() {
        redisTestService.performRedisTest();
        return "Redis test executed";
    }

    @GetMapping("/redis-test2")
    @ResponseBody
    public String testRedisConnection() {
        boolean result = redissonTestService.testConnection();
        return result ? "Redis 연결 성공!" : "Redis 연결 실패!";
    }

    @GetMapping("/redis-test3")
    public String test3() {
        int gamePk = 100;
        int userPk = 3;
        List<String> seats = Arrays.asList("A1", "A2");

        //선점
        boolean preempted = redisService.preemptSeat(gamePk, seats, userPk);
        log.info("preempted: " + preempted);

        return "성공";
    }

    @GetMapping("/redis-test4")
    public String test4() {
        int gamePk = 1;
        int userPk = 11;

        boolean getLock = redisService.tryLockSeat(gamePk, null);

        log.info("getLock: " + getLock);

        if(getLock) {
            try { Thread.sleep(10000); } catch (InterruptedException e) {}
            redisService.unlockSeat(1, null);
        }

        return "성공";
    }
}
