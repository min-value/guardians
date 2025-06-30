package org.baseball.domain.tickets;

import org.apache.ibatis.annotations.Mapper;
import org.baseball.dto.PredictInfoDTO;
import org.baseball.dto.TicketsDTO;

import java.util.List;
import java.util.Map;

@Mapper
public interface TicketsMapper {
    List<TicketsDTO> getTicketsList(Map<String, Object> param);
    int countTicketsList(Map<String, Object> param);
    int updatePredict(Map<String, Object> param);
    PredictInfoDTO getGameInfoforPredict(int gamePk);
    int updateReserveList(Map<String, Object> param);
    int updateReservations(Map<String, Object> param);
    int deductPoint(Map<String, Object> param);
    int insertReservePointUsage(Map<String, Object> param);
}
