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


    public String getLockKey(int gamePk, String seatNum, int zonePk) {
        StringBuilder sb = new StringBuilder("lock:seat:" + gamePk + ":" + zonePk + ":");

        if(seatNum == null || seatNum.isEmpty()){
            sb.append("auto");
        } else {
            sb.append(seatNum);
        }

        return sb.toString();
    }

    public String getPreemptkey(int gamePk, String seatNum, int zonePk, int userPk) {
        StringBuilder sb = new StringBuilder("preempt:seat:" + gamePk + ":" + zonePk + ":");

        if(seatNum == null || seatNum.isEmpty()){
            sb.append("auto:").append(userPk);
        } else {
            sb.append(seatNum);
        }

        return sb.toString();
    }

    public String getPaidKey(int gamePk, String seatNum, int zonePk, int userPk) {
        StringBuilder sb = new StringBuilder("paid:seat:" + gamePk + ":" + zonePk + ":");

        if(seatNum == null || seatNum.isEmpty()){
            sb.append("auto:").append(userPk);
        } else {
            sb.append(seatNum);
        }

        return sb.toString();
    }
    //lock 획득 -> (seatNum=null) lock:seat:{gamePk}:{zonePk}:auto / lock:seat:{gamePk}:{zonePk}:{seatNum}
    public boolean tryLockSeat(int gamePk, String seatNum, int zonePk) {
        //락 키 생성
        String lockKey = getLockKey(gamePk, seatNum, zonePk);

        //락 생성
        RLock lock = redissonClient.getLock(lockKey);

        //락 시도
        try {
            if(lock.tryLock(3, 10, TimeUnit.SECONDS)) {
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

    //lock 해제 -> lock:seat:{gamePk}:{zonePk}:auto
    public void unlockSeat(int gamePk, String seatNum, int zonePk) {
        //락 키 생성
        String lockKey = getLockKey(gamePk, seatNum, zonePk);

        RLock lock = redissonClient.getLock(lockKey);

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

    //외야석 선점 구현(TTL을 위함) -> preempt:seat:{gamePk}:{zonePk}:자동배정:{userPk}
    public boolean getBleachers(int gamePk, int userPk, int zonePk) {
        String key = getPreemptkey(gamePk, null, zonePk, userPk);

        if(redisTemplate.hasKey(key)) {
            return false;
        }

        return Boolean.TRUE.equals(redisTemplate.opsForValue()
                .setIfAbsent(key, String.valueOf(userPk), 1, TimeUnit.MINUTES)); //todo
    }

    //타 구역에 대한 선점 등록(TTL: 10M) -> lock:seat:{gamePk}:{zonePk}:{seatNum} / preempt:seat:{gamePk}:{zonePk}:{seatNum}
    public boolean preemptSeat(int gamePk, List<String> seats, int userPk, int zonePk) {
        List<RLock> locks = new ArrayList<>();
        List<String> preemptedKeys = new ArrayList<>();

        try {
            //락 리스트 생성
            for(String seatNum: seats) {
                RLock lock = redissonClient.getLock(getLockKey(gamePk, seatNum, zonePk));
                locks.add(lock);
            }

            //Multilock 획득 시도
            RLock multiLock = redissonClient.getMultiLock(locks.toArray(new RLock[0]));

            if(!multiLock.tryLock(3, 10, TimeUnit.SECONDS)) {
                //락 획득 실패
                return false;
            }

            //이미 선점/판매된 좌석이 있는지 확인
            for(String seatNum: seats) {
                String preemptKey = getPreemptkey(gamePk, seatNum, zonePk, userPk);
                String paidKey = getPaidKey(gamePk,  seatNum, zonePk, userPk);

                String existingPreempt = redisTemplate.opsForValue().get(preemptKey);
                String existingPaid = redisTemplate.opsForValue().get(paidKey);

                if(existingPreempt != null || existingPaid != null) {
                    //이미 선점/판매된 좌석 발견 -> 전체 실패
                    return false;
                }
            }

            //모든 좌석에 대해 선점 등록
            for(String seatNum: seats) {
                String preemptKey = getPreemptkey(gamePk, seatNum, zonePk, userPk);
                redisTemplate.opsForValue().set(preemptKey, String.valueOf(userPk), 1, TimeUnit.MINUTES); //todo: minute으로 변경
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

    //타 구역에 대한 선점 해제 및 결제완료 등록(TTL X) -> preempt:seat:{gamePk}:{zonePk}:{seatNum} / paid:seat:{gamePk}:{zonePk}:{seatNum}
    public boolean confirmPayment(int gamePk, List<String> seats, int userPk, int zonePk) {
        List<RLock> locks = new ArrayList<>();
        List<String> paidKeys = new ArrayList<>();
        List<String> preemptKeys =  new ArrayList<>();

        try {
            //락 리스트 생성
            for(String seatNum: seats) {
                RLock lock = redissonClient.getLock(getLockKey(gamePk, seatNum, zonePk));
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
                String preemptKey = getPreemptkey(gamePk, seatNum, zonePk, userPk);
                String preemptUser = redisTemplate.opsForValue().get(preemptKey);
                preemptKeys.add(preemptKey);

                if(!String.valueOf(userPk).equals(preemptUser)) {
                    return false;
                }

                String paidKey = getPaidKey(gamePk, seatNum, zonePk, userPk);
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
    public boolean confirmPreempt(int gamePk, String seatNum, int userPk, int zonePk) {
        String preemptKey = getPreemptkey(gamePk, seatNum, zonePk, userPk);

        String userConfirm = redisTemplate.opsForValue().get(preemptKey.toString());
        return String.valueOf(userPk).equals(userConfirm);
    }

    //외야석 선점 취소 -> preempt:seat:{gamePk}:{zonePk}:auto:{userPk} 삭제
    public boolean cancelBleachers(int gamePk, int userPk, int zonePk) {
        String key = getPreemptkey(gamePk, null, zonePk, userPk);

        // 삭제 시도
        Boolean result = redisTemplate.delete(key);
        return Boolean.TRUE.equals(result);
    }

    //선점 취소
    public boolean cancelPreempt(int gamePk, List<String> seats, int userPk, int zonePk) {
        List<RLock> locks = new ArrayList<>();

        try {
            // 락 리스트 생성
            for (String seatNum : seats) {
                RLock lock = redissonClient.getLock(getLockKey(gamePk, seatNum, zonePk));
                locks.add(lock);
            }

            // MultiLock 획득 시도
            RLock multiLock = redissonClient.getMultiLock(locks.toArray(new RLock[0]));
            if (!multiLock.tryLock(3, 10, TimeUnit.SECONDS)) {
                return false; // 락 실패
            }

            // 각 선점 키가 본인 것인지 확인 후 삭제
            for (String seatNum : seats) {
                String preemptKey = getPreemptkey(gamePk, seatNum, zonePk, userPk);
                String owner = redisTemplate.opsForValue().get(preemptKey);

                if (String.valueOf(userPk).equals(owner)) {
                    redisTemplate.delete(preemptKey);
                } else {
                    // 내 선점이 아닌 좌석이 있으면 전체 실패 처리
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // 락 해제
            for (RLock lock : locks) {
                if (lock.isHeldByCurrentThread()) {
                    try {
                        lock.unlock();
                    } catch (Exception ignore) {}
                }
            }
        }
    }

    //결제 취소
    public boolean cancelPayment(int gamePk, List<String> seats, int userPk, int zonePk) {
        List<RLock> locks = new ArrayList<>();

        try {
            // 락 리스트 생성
            for (String seatNum : seats) {
                RLock lock = redissonClient.getLock(getLockKey(gamePk, seatNum, zonePk));
                locks.add(lock);
            }

            // MultiLock 획득 시도
            RLock multiLock = redissonClient.getMultiLock(locks.toArray(new RLock[0]));
            if (!multiLock.tryLock(3, 10, TimeUnit.SECONDS)) {
                return false;
            }

            // paid 키가 본인 것인지 확인 후 삭제
            for (String seatNum : seats) {
                String paidKey = getPaidKey(gamePk, seatNum, zonePk, userPk);
                String owner = redisTemplate.opsForValue().get(paidKey);

                if (String.valueOf(userPk).equals(owner)) {
                    redisTemplate.delete(paidKey);
                } else {
                    // 내 결제가 아닌 경우 전체 실패 처리
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // 락 해제
            for (RLock lock : locks) {
                if (lock.isHeldByCurrentThread()) {
                    try {
                        lock.unlock();
                    } catch (Exception ignore) {}
                }
            }
        }
    }

}
