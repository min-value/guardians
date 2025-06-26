package org.baseball.domain.reservation;

import lombok.extern.slf4j.Slf4j;
import org.baseball.dto.ReserveGameInfoDTO;
import org.baseball.dto.SoldSeatsReqDTO;
import org.baseball.dto.ZoneDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ReservationService {
    @Autowired
    private ReservationMapper reservationMapper;

    //게임 정보(상대팀) 반환
    public ReserveGameInfoDTO getGameInfo(int gamePk) {
            return reservationMapper.getGameInfo(gamePk);
    }

    //팔린 좌석 목록 반환
    public List<String> getSoldSeats(SoldSeatsReqDTO dto) {
        return reservationMapper.getSoldSeats(dto);
    }

    //구역 정보 반환
    public List<ZoneDTO> getZones() {
        return reservationMapper.getZones();
    }

    //구역 당 남은 좌석 수 계산한 map 반환
    public int calRemainingNum(int total, int sold) {
        return total - sold;
    }
}
