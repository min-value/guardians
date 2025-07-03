package org.baseball.domain.admin;

import org.baseball.dto.AddGameInfoDTO;
import org.baseball.dto.GamesInfoDTO;
import org.baseball.dto.HomeGameDTO;
import org.baseball.dto.ReserveInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    @Autowired
    AdminMapper adminMapper;

    public List<ReserveInfoDTO> showReservationList(int page){
        int size = 7;
        int offset = (page - 1) * size;

        Map<String, Object> param = new HashMap<>();
        param.put("offset", offset);
        param.put("size", size);

        List<ReserveInfoDTO> list = adminMapper.showReservationList(param);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // 초 없이
        int cnt =1+offset;
        for (ReserveInfoDTO l : list) {
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
                String row = seat.substring(0, 1);
                String number = seat.substring(1);
                if(seats[i].equals("자동배정")){
                    zoneAndSeat[i] = zones[i] + "구역 " + seat + "석";
                }else {
                    zoneAndSeat[i] = zones[i] + "구역 " + row + "열 " + number + "석";
                }
            }
            l.setZoneAndSeat(zoneAndSeat);
        }
        return list;
    }

    public List<GamesInfoDTO> showGamesList(String status, int page){
        int size = 7;
        int offset = (page - 1) * size;
        Map<String, Object> param = new HashMap<>();
        param.put("status", status);
        param.put("size", size);
        param.put("offset", offset);

        List<GamesInfoDTO> list = adminMapper.showGamesList(param);
        int cnt =1+offset;
        for (GamesInfoDTO l : list){
            l.setNo(cnt++);
            l.setOurTeam("6");
            l.setStatus(1);
            if(l.getResult() == null) {
                l.setResult("예정");
                l.setStatus(2);
                l.setOpponentScore("-");
                l.setOurScore("-");
            }
            else {
                switch (l.getResult()) {
                    case "WIN":
                        l.setResult("승리");
                        break;
                    case "LOSE":
                        l.setResult("패배");
                        break;
                    case "DRAW":
                        l.setResult("무승부");
                        break;
                    case "CANCEL":
                        l.setResult("취소");
                        l.setOpponentScore("-");
                        l.setOurScore("-");
                        break;
                }
            }
        }
        return list;
    }

    public List<GamesInfoDTO> showGamesAddList(int page){
        int size = 7;
        int offset = (page - 1) * size;

        Map<String, Object> param = new HashMap<>();
        param.put("offset", offset);
        param.put("size", size);

        List<GamesInfoDTO> list = adminMapper.showGamesAddList(param);
        int cnt =1+offset;
        for (GamesInfoDTO l : list){
            l.setNo(cnt++);
            l.setOurTeam("6");
            l.setResult("예정");
            l.setStatus(2);
            l.setOpponentScore("-");
            l.setOurScore("-");
        }
        return list;
    }

    public AddGameInfoDTO showAddGameInfo(int gameNo) {
        return adminMapper.showAddGameInfo(gameNo);
    }

    public boolean addHomeGame(HomeGameDTO homeGameDTO) {
        int r = adminMapper.addHomeGame(homeGameDTO);
        return r > 0;
    }

    public int countReservation(){
        return adminMapper.countReservation();
    }

    public int countGames(String status){
        Map<String, Object> param = new HashMap<>();
        param.put("status", status);
        return adminMapper.countGames(param);
    }

    public int countHomeGames(){
        return adminMapper.countHomeGames();
    }

}
