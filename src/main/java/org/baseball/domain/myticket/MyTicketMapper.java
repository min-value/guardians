package org.baseball.domain.myticket;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MyTicketMapper {
    List<Map<String, Object>> selectTicketsByUserPk(int userPk);
    int cancelReservationList(int reservelistPk);
    int cancelReservations(int reservelistPk);
    int insertRefundPoint(Map<String, Object> map);
    int updateRefundPoint(Map<String, Object> map);
}
