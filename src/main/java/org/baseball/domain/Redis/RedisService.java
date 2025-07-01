package org.baseball.domain.Redis;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisService {
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate redisTemplate;


    //lock 획득 -> (seatNum=null) lock:seat:{gamePk}:auto / lock:seat:{gamePk}:{seatNum}
    public boolean tryLockSeat(int gamePk, String seatNum) {
        //락 키 생성
        StringBuilder lockKey = new StringBuilder("lock:seat:" + gamePk + ":");

        if(seatNum == null || seatNum.isEmpty()) {
            lockKey.append("auto");
        } else {
            lockKey.append(seatNum);
        }

        //락 생성
        RLock lock = redissonClient.getLock(lockKey.toString());

        //락 시도
        try {
            if(lock.tryLock(3, 30, TimeUnit.SECONDS)) {
                //락 시도 성공 시
                log.info("락 획득: {}, thread: {}", lockKey, Thread.currentThread().getId());
                return true;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        //락 시도 실패 시
        return false;
    }

    //lock 해제 -> lock:seat:{gamePk}:auto
    public void unlockSeat(int gamePk, String seatNum) {
        //락 키 생성
        StringBuilder lockKey = new StringBuilder("lock:seat:" + gamePk + ":");

        if(seatNum == null || seatNum.isEmpty()) {
            lockKey.append("auto");
        } else {
            lockKey.append(seatNum);
        }

        RLock lock = redissonClient.getLock(lockKey.toString());

        //락 해제
        try {
            log.warn("락 해제 시도 thread: {}", Thread.currentThread().getId());
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        } catch (IllegalMonitorStateException e) {
            log.info("락 해제 실패: 락 소유자가 아님", e);
        }
    }

    //타 구역에 대한 선점 등록(TTL: 10M) -> preempt:seat:{gamePk}:{seatNum} / paid:seat:{gamePk}:{seatNum}
    public boolean preemptSeat(int gamePk, List<String> seats, int userPk) {
        List<RLock> locks = new ArrayList<>();
        List<String> preemptedKeys = new ArrayList<>();

        try {
            //락 리스트 생성
            for(String seatNum: seats) {
                RLock lock = redissonClient.getLock("lock:seat:" + gamePk + ":" + seatNum);
                locks.add(lock);
            }

            //Multilock 획득 시도
            RLock multiLock = redissonClient.getMultiLock(locks.toArray(new RLock[0]));

            if(!multiLock.tryLock(3, 30, TimeUnit.SECONDS)) {
                //락 획득 실패
                return false;
            }

            //이미 선점/판매된 좌석이 있는지 확인
            for(String seatNum: seats) {
                String preemptKey = "preempt:seat:" + gamePk + ":" + seatNum;
                String paidKey = "paid:seat:" + gamePk + ":" + seatNum;

                String existingPreempt = redisTemplate.opsForValue().get(preemptKey);
                String existingPaid = redisTemplate.opsForValue().get(paidKey);

                if(existingPreempt != null || existingPaid != null) {
                    //이미 선점/판매된 좌석 발견 -> 전체 실패
                    throw new IllegalStateException("해당 좌석은 이미 선점된 좌석입니다");
                }
            }

            //모든 좌석에 대해 선점 등록
            for(String seatNum: seats) {
                String preemptKey = "preempt:seat:" + gamePk + ":" + seatNum;
                redisTemplate.opsForValue().set(preemptKey, String.valueOf(userPk), 10, TimeUnit.MINUTES);
                preemptedKeys.add(preemptKey); //나중에 실패 시 롤백용
            }
            return true;
        } catch (Exception e) {
            //롤백
            for(String key: preemptedKeys) {
                redisTemplate.delete(key);
            }
            return false;
        } finally {
            //락 해제
            for(RLock lock: locks) {
                if (lock.isHeldByCurrentThread()) {
                    try {
                        lock.unlock();
                    } catch (Exception ignore) {}
                }
            }
        }
    }

    //타 구역에 대한 선점 해제 및 결제완료 등록(TTL X) -> preempt:seat:{gamePk}:{seatNum} / paid:seat:{gamePk}:{seatNum}
    public boolean confirmPayment(int gamePk, List<String> seats, int userPk) {
        List<RLock> locks = new ArrayList<>();
        List<String> paidKeys = new ArrayList<>();
        List<String> preemptKeys =  new ArrayList<>();

        try {
            //락 리스트 생성
            for(String seatNum: seats) {
                RLock lock = redissonClient.getLock("lock:seat" + gamePk + ":" + seatNum);
                locks.add(lock);
            }

            //Multilock 획득 시도
            RLock multiLock = redissonClient.getMultiLock(locks.toArray(new RLock[0]));

            //락 획득 실패 시
            if(!multiLock.tryLock(3, 30, TimeUnit.SECONDS)) {
                return false;
            }

            //이미 선점/판매된 좌석이 있는지 확인
            for(String seatNum: seats) {
                String preemptKey = "preempt:seat:" + gamePk + ":" + seatNum;
                String preemptUser = redisTemplate.opsForValue().get(preemptKey);
                preemptKeys.add(preemptKey);

                if(!String.valueOf(userPk).equals(preemptUser)) {
                    throw new IllegalStateException("해당 좌석은 이미 선점된 좌석입니다");
                }

                String paidKey = "paid:seat:" + gamePk + ":" + seatNum;
                redisTemplate.opsForValue().set(paidKey, String.valueOf(userPk));
                paidKeys.add(paidKey); //나중에 실패 시 롤백 용
            }

            return true;
        } catch (Exception e) {
            //롤백
            for(String key: paidKeys) {
                redisTemplate.delete(key);
            }

            //선점 해제
            for(String key: preemptKeys) {
                redisTemplate.delete(key);
            }

            return false;
        } finally {
            //락 해제
            for(RLock lock: locks) {
                if(lock.isHeldByCurrentThread()) {
                    try {
                        lock.unlock();
                    } catch (Exception ignore) {}
                }
            }
        }
    }

    //선점 TTL 유효한지 확인
    public boolean confirmPreempt(int gamePk, String seatNum, int userPk) {
        String preemptKey = "preempt:seat:" + gamePk + ":" + seatNum;
        String userConfirm = redisTemplate.opsForValue().get(preemptKey);

        return String.valueOf(userPk).equals(userConfirm);
    }
}
