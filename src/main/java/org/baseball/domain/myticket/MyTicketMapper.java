package org.baseball.domain.myticket;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MyTicketMapper {
    List<Map<String, Object>> selectTicketsByUserPk(int userPk);
    int cancelReservationList(Map<String, Object> map);
    int cancelReservations(Map<String, Object> map);
    int insertRefundPoint(Map<String, Object> map);
    int updateRefundPoint(Map<String, Object> map);
    int restoreReservationList(Map<String, Object> map);
    int restoreReservations(Map<String, Object> map);
    int deleteRefundPoint(Map<String, Object> map);
    int restoreRefundPoint(Map<String, Object> map);
}
