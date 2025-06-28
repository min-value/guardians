package org.baseball.domain.reservation;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.baseball.dto.*;

import java.util.List;

@Mapper
public interface ReservationMapper {
    ReserveGameInfoDTO getGameInfo(int gamePk);
    List<String> getSoldSeats(SoldSeatsReqDTO dto);
    List<ZoneDTO> getZones();
    int confirmPreemption(@Param("zonePk") int zonePk,@Param("seatNum") String seatNum,@Param("gamePk") int gamePk);
    void setPreemptionList(PreemptionListDTO dto);
    void setPreemptionReserve(PreemptionReserveDTO dto);
    void deletePreemptionList(int reservelistPk);
    void deletePreemptionReserve(int reservelistPk);
}

