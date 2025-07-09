package org.baseball.domain.redis;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class QueueService {

    @Autowired
    private RedissonClient redissonClient;

    private String getQueueKey(int gamePk) {
        return "reservation:queue:" + gamePk;
    }

    public boolean enqueueUser(int gamePk, int userPk) {
        RScoredSortedSet<String> queue = redissonClient.getScoredSortedSet(getQueueKey(gamePk));
        String user = String.valueOf(userPk);
        if (!queue.contains(user)) {
            double score = System.currentTimeMillis();
            queue.add(score, user);
            return true;
        }
        return false;
    }

    public boolean canReserve(int gamePk, int userPk) {
        RScoredSortedSet<String> queue = redissonClient.getScoredSortedSet(getQueueKey(gamePk));
        String first = queue.first();
        return String.valueOf(userPk).equals(first);
    }

    public boolean dequeueUser(int gamePk, int userPk) {
        RScoredSortedSet<String> queue = redissonClient.getScoredSortedSet(getQueueKey(gamePk));
        return queue.remove(String.valueOf(userPk));
    }

    public int getQueuePosition(int gamePk, int userPk) {
        RScoredSortedSet<String> queue = redissonClient.getScoredSortedSet(getQueueKey(gamePk));
        String user = String.valueOf(userPk);
        return queue.rank(user) != null ? queue.rank(user) + 1 : -1;
    }

    public int getQueueSize(int gamePk) {
        return redissonClient.getScoredSortedSet(getQueueKey(gamePk)).size();
    }

    public void notifyNext(int gamePk) {
        RScoredSortedSet<String> queue = redissonClient.getScoredSortedSet(getQueueKey(gamePk));
        String nextUser = queue.first();
        if (nextUser != null) {
            int userPk = Integer.parseInt(nextUser);
            log.info("예약 가능 유저: gamePk={}, userPk={}", gamePk, userPk);
        }
    }

    public void pollFront(int gamePk) {
        RScoredSortedSet<String> queue = redissonClient.getScoredSortedSet(getQueueKey(gamePk));
        String first = queue.first();
        if (first != null) {
            queue.remove(first);
        }
    }
}
