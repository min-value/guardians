package org.baseball.domain.tickets;

import org.apache.ibatis.annotations.Mapper;
import org.baseball.dto.PredictInfoDTO;
import org.baseball.dto.PurchaseReqDTO;
import org.baseball.dto.TicketsDTO;

import java.util.List;
import java.util.Map;

@Mapper
public interface TicketsMapper {
    List<TicketsDTO> getTicketsList(Map<String, Object> param);
    int countTicketsList(Map<String, Object> param);
    int updatePredict(Map<String, Object> param);
    PredictInfoDTO getGameInfoforPredict(int gamePk);
    int updateReserveList(PurchaseReqDTO dto);
    int updateReservations(PurchaseReqDTO dto);
    int deductPoint(PurchaseReqDTO dto);
    int insertReservePointUsage(PurchaseReqDTO dto);
}
