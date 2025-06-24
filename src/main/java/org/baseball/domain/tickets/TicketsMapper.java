package org.baseball.domain.tickets;

import org.apache.ibatis.annotations.Mapper;
import org.baseball.dto.TicketsDTO;

import java.util.List;
import java.util.Map;

@Mapper
public interface TicketsMapper {
    List<TicketsDTO> getTicketsList(Map<String, Object> param);
    int countTicketsList();
}
