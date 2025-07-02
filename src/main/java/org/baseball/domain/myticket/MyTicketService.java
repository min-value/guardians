package org.baseball.domain.myticket;

import java.util.List;
import java.util.Map;

public interface MyTicketService {
    List<Map<String, Object>> getTicketsByUserPk(int userPk);
    int cancelReservationList(int reservelistPk);
    int cancelReservations(int reservelistPk);
    int insertRefundPoint(Map<String, Object> map);
    int updateRefundPoint(Map<String, Object> map);
}
