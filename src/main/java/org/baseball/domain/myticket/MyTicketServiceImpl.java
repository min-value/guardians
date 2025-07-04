package org.baseball.domain.myticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public boolean cancelReservation(Map<String, Object> map) {
        int updated1 = myTicketMapper.cancelReservationList(map);
        int updated2 = myTicketMapper.cancelReservations(map);
        int updated3 = myTicketMapper.insertRefundPoint(map);
        int updated4 = myTicketMapper.updateRefundPoint(map);

        return updated1>0 && updated2>0 && updated3>0 && updated4>0;
    }

    @Override
    public boolean restoreCanceledReservation(Map<String, Object> map){
        int updated1 = myTicketMapper.restoreReservationList(map);
        int updated2 = myTicketMapper.restoreReservations(map);
        int updated3 = myTicketMapper.deleteRefundPoint(map);
        int updated4 = myTicketMapper.restoreRefundPoint(map);

        return updated1>0 && updated2>0 && updated3>0 && updated4>0;
    }
}

