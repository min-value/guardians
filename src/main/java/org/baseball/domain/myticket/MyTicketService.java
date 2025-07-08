package org.baseball.domain.myticket;

import java.util.List;
import java.util.Map;

public interface MyTicketService {
    List<Map<String, Object>> getTicketsByUserPk(int userPk);
    boolean cancelReservation(Map<String, Object> map);
    boolean restoreCanceledReservation(Map<String, Object> map);
}
