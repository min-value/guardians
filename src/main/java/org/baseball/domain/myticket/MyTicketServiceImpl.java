package org.baseball.domain.myticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MyTicketServiceImpl implements MyTicketService {

    private final MyTicketMapper myTicketMapper;

    @Autowired
    public MyTicketServiceImpl(MyTicketMapper myTicketMapper) {
        this.myTicketMapper = myTicketMapper;
    }

    @Override
    public List<Map<String, Object>> getTicketsByUserPk(int userPk) {
        return myTicketMapper.selectTicketsByUserPk(userPk);
    }
}

