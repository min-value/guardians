package org.baseball.domain.admin;

import org.baseball.dto.ReserveInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    AdminMapper adminMapper;

    public List<ReserveInfoDto> showReserveList(){
        List<ReserveInfoDto> list = adminMapper.showReserveList();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // 초 없이
        int cnt =1;
        for (ReserveInfoDto l : list) {
            l.setNo(cnt++);
            if (l.isCancel()) {
                l.setStatus("취소완료");
            } else if (l.isPay() && l.getGameDate().after(now)) {
                l.setStatus("결제완료");
            } else if (l.isPay() && l.getGameDate().before(now)) {
                l.setStatus("사용완료");
            }

            if (l.getGameDate() != null)
                l.setGameDateFormat(dateFormat.format(l.getGameDate()));
            if (l.getCancelDate() != null)
                l.setCancelDateFormat(dateFormat.format(l.getCancelDate()));
            else{
                l.setCancelDateFormat("-");
            }

            String[] zones = l.getZone() != null ? l.getZone().split(",") : new String[0];
            String[] seats = l.getSeat() != null ? l.getSeat().split(",") : new String[0];
            l.setZones(zones);
            l.setSeats(seats);

            String[] zoneAndSeat = new String[zones.length];
            for (int i = 0; i < zones.length; i++) {
                String seat = seats[i];
                String row = seat.substring(0, 1);  // 예: A
                String number = seat.substring(1); // 예: 2
                zoneAndSeat[i] = zones[i] + "구역 " + row + "열 " + number + "번";
            }
            l.setZoneAndSeat(zoneAndSeat);
        }

        return list;

    }
}
