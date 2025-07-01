package org.baseball.domain.myticket;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MyTicketMapper {
    List<Map<String, Object>> selectTicketsByUserPk(int userPk);
}
