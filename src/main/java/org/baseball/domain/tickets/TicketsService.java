package org.baseball.domain.tickets;

import org.baseball.dto.PredictInfoDTO;
import org.baseball.dto.TicketsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketsService {
    @Autowired
    TicketsMapper ticketsMapper;

    public List<TicketsDTO> getTicketsList(int page, int teamStatus, int ticketStatus) {
        int size = 6;
        int offset = (page - 1) * size;

        Map<String, Object> param = new HashMap<>();
        param.put("offset", offset);
        param.put("size", size);
        param.put("teamStatus", teamStatus);
        param.put("ticketStatus", ticketStatus);

        System.out.println("== 쿼리 요청 param ==");
        System.out.println("teamStatus: " + teamStatus);
        System.out.println("ticketStatus: " + ticketStatus);
        System.out.println("offset: " + offset);
        System.out.println("size: " + size);

        List<TicketsDTO> list = ticketsMapper.getTicketsList(param);

        for(TicketsDTO l : list) {
            l.setOurTeam("6");
            l.setDay(l.getDays()[l.getDayIndex()]);
            LocalDateTime localDateTime = l.getGameDate().toLocalDateTime();
            l.setDate(localDateTime.format(DateTimeFormatter.ofPattern("MM.dd")));
            l.setTime(localDateTime.format(DateTimeFormatter.ofPattern("HH:mm")));
        }
        return list;
    }

    public int countTicketsList(int teamStatus, int ticketStatus) {
        Map<String, Object> param = new HashMap<>();
        param.put("teamStatus", teamStatus);
        param.put("ticketStatus", ticketStatus);
        return ticketsMapper.countTicketsList(param);
    }

    public int updatePredict(int userPk, int reservelistPk, int predict) {
        Map<String, Object> param = new HashMap<>();
        param.put("userPk", userPk);
        param.put("reservelistPk", reservelistPk);
        param.put("predict", predict);
        return ticketsMapper.updatePredict(param);
    }

    public PredictInfoDTO getGameInfoforPredict(int gamePk) {
        return ticketsMapper.getGameInfoforPredict(gamePk);
    }

    @Transactional
    public boolean updatePurchase(Map<String, Object> param) {
        System.out.println("param: " + param);

        int updated1 = ticketsMapper.updateReserveList(param);
        int updated2 = ticketsMapper.updateReservations(param);
        int updated3 = ticketsMapper.deductPoint(param);
        int updated4 = ticketsMapper.insertReservePointUsage(param);

        return updated1 > 0 && updated2 > 0 && updated3 > 0 && updated4 > 0;
    }


}
