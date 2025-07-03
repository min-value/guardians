package org.baseball.domain.Redis;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class QueueService {

    @Autowired
    private RedissonClient redissonClient;

    public String getQueueKey(int gamePk) {
        return "reservation:queue:" + gamePk;
    }

    public boolean enqueueUser(int gamePk, int userPk) {
        RQueue<String> queue = redissonClient.getQueue(getQueueKey(gamePk));
        if (!queue.contains(String.valueOf(userPk))) {
            queue.add(String.valueOf(userPk));
            return true;
        }
        return false;
    }

    public boolean canReserve(int gamePk, int userPk) {
        RQueue<String> queue = redissonClient.getQueue(getQueueKey(gamePk));
        return String.valueOf(userPk).equals(queue.peek());
    }

    public boolean dequeueUser(int gamePk, int userPk) {
        RQueue<String> queue = redissonClient.getQueue(getQueueKey(gamePk));
        return queue.remove(String.valueOf(userPk));
    }

    public int getQueuePosition(int gamePk, int userPk) {
        List<Object> list = redissonClient.getQueue(getQueueKey(gamePk)).readAll();
        return list.indexOf(String.valueOf(userPk)) + 1;
    }

    public void notifyNext(int gamePk) {
        String nextUser = (String) redissonClient.getQueue(getQueueKey(gamePk)).peek();
        if (nextUser != null) {
            int userPk = Integer.parseInt(nextUser);
            // 예: WebSocket 알림
        }
    }
}
