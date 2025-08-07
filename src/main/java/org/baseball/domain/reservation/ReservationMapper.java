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
    ZoneDTO getZoneInfo(@Param("zonePk") int zonePk);
    int confirmPreemption(@Param("zonePk") int zonePk,@Param("seatNum") String seatNum,@Param("gamePk") int gamePk);
    void setPreemptionList(PreemptionListDTO dto);
    void setPreemptionReserve(PreemptionReserveDTO dto);
    void deletePreemptionList(int reservelistPk);
    void deletePreemptionReserve(int reservelistPk);
    List<DiscountDTO> getDiscountInfo();
    boolean isOurGame(int gamePk);
    Integer getReservelistPk(@Param("gamePk") int gamePk, @Param("seatNum") String seatNum, @Param("zonePk") int zonePk);
    Integer getReservelistPkByUser(@Param("gamePk") int gamePk, @Param("seatNum") String seatNum, @Param("zonePk") int zonePk, @Param("userPk") int userPk);
    Integer getReservelistPkAuto(@Param("gamePk") int gamePk, @Param("userPk") int userPk, @Param("zonePk") int zonePk);
    int countUserReserve(@Param("gamePk") int gamePk, @Param("userPk") int userPk);
    List<HeldSeatDTO> selectHeldAll(@Param("gamePk") int gamePk, @Param("userPk") int userPk);
}

