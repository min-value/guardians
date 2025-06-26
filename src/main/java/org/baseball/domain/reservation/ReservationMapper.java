package org.baseball.domain.reservation;

import org.apache.ibatis.annotations.Mapper;
import org.baseball.dto.ReserveGameInfoDTO;
import org.baseball.dto.SoldSeatsReqDTO;
import org.baseball.dto.ZoneDTO;

import java.util.List;

@Mapper
public interface ReservationMapper {
    ReserveGameInfoDTO getGameInfo(int gamePk);
    List<String> getSoldSeats(SoldSeatsReqDTO dto);
    List<ZoneDTO> getZones();
}

