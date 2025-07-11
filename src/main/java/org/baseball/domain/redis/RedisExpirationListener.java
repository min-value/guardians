package org.baseball.domain.redis;

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
            String seatNum = parts[4];
            int zonePk = Integer.parseInt(parts[3]);
            Integer reservelistPk = null;

            if(parts.length == 6) {
                //자동 배정(gamePk, userPk로 삭제)
                int userPk = Integer.parseInt(parts[5]);

                //로컬 DB 삭제 로직
                reservelistPk = reservationMapper.getReservelistPkAuto(gamePk, userPk, zonePk);
                log.info("리스너-reservelistPk (type: {}): {}",
                        (reservelistPk == null ? "null" : reservelistPk.getClass().getSimpleName()),
                        reservelistPk);


                if(reservelistPk == null) {
                    log.warn("해당 예약 없음: 자동배정");
                    return;
                }

                reservationMapper.deletePreemptionReserve(reservelistPk);
                reservationMapper.deletePreemptionList(reservelistPk);
            } else if(parts.length == 5) {
                //그 외 구역

                //로컬 DB 삭제 로직
                reservelistPk = reservationMapper.getReservelistPk(gamePk, seatNum, zonePk);
                log.info("리스너-reservelistPk (type: {}): {}",
                        (reservelistPk == null ? "null" : reservelistPk.getClass().getSimpleName()),
                        reservelistPk);


                if(reservelistPk == null) {
                    log.warn("해당 예약 없음: 자동배정X");
                    return;
                }

                reservationMapper.deletePreemptionReserve(reservelistPk);
                reservationMapper.deletePreemptionList(reservelistPk);
            }
            log.info("gamePk={" + gamePk + "} seat={" + seatNum + "} reservelistPk={" + reservelistPk + "} 선점 정보 DB에서 제거");
        }
    }
}
