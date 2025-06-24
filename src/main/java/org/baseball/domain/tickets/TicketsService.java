package org.baseball.domain.tickets;

import org.baseball.dto.TicketsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketsService {
    @Autowired
    TicketsMapper ticketsMapper;

    public List<TicketsDTO> getTicketsList(int page) {
        int size = 6;
        int offset = (page - 1) * size;

        Map<String, Object> param = new HashMap<>();
        param.put("offset", offset);
        param.put("size", size);

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

    public int countTicketsList() {
        return ticketsMapper.countTicketsList();
    }
}
