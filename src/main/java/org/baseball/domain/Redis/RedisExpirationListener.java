package org.baseball.domain.Redis;

import lombok.extern.slf4j.Slf4j;
import org.baseball.domain.reservation.ReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Slf4j
public class RedisExpirationListener extends KeyExpirationEventMessageListener {
    @Autowired
    ReservationMapper reservationMapper;

    public RedisExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        log.info("만료된 Redis 키 감지: {}", expiredKey);
        if (expiredKey.startsWith("preempt:seat")) {
            String[] parts = expiredKey.split(":");
            int gamePk = Integer.parseInt(parts[2]);
            String seatNum = parts[3];

            if(parts.length == 5) {
                //자동 배정(gamePk, userPk로 삭제)
                int userPk = Integer.parseInt(parts[4]);

                //로컬 DB 삭제 로직
                int reservelistPk = reservationMapper.getReservelistPkAuto(gamePk, userPk);
                if(reservelistPk == -1) {
                    log.warn("해당 예약 없음");
                }
                reservationMapper.deletePreemptionReserve(reservelistPk);
                reservationMapper.deletePreemptionList(reservelistPk);
            } else if(parts.length == 4) {
                //그 외 구역

                //로컬 DB 삭제 로직
                int reservelistPk = reservationMapper.getReservelistPk(gamePk, seatNum);
                if(reservelistPk == -1) {
                    log.warn("해당 예약 없음");
                }
                reservationMapper.deletePreemptionReserve(reservelistPk);
                reservationMapper.deletePreemptionList(reservelistPk);
            }
            log.info("gamePk={} seat={} 선점 정보 DB에서 제거", gamePk, seatNum);
        }
    }
}
