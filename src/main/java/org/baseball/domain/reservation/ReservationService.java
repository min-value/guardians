package org.baseball.domain.reservation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.baseball.dto.ReserveGameInfoDTO;
import org.baseball.dto.SoldSeatsReqDTO;
import org.baseball.dto.ZoneDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ReservationService {
    @Autowired
    private ReservationMapper reservationMapper;

    //게임 정보(상대팀) 반환
    public ReserveGameInfoDTO getGameInfo(int gamePk) {
            return reservationMapper.getGameInfo(gamePk);
    }

    //팔린 좌석 목록 반환
    public List<String> getSoldSeats(SoldSeatsReqDTO dto) {
        return reservationMapper.getSoldSeats(dto);
    }

    //구역 정보 반환
    public List<ZoneDTO> getZones() {
        return reservationMapper.getZones();
    }

    //구역 당 남은 좌석 수 계산한 map 반환
    public int calRemainingNum(int total, int sold) {
        return total - sold;
    }

    // 구역 정보 가져오기
    public boolean getSeatInfo(int gamePk, HttpSession session) throws JsonProcessingException {
        //구역 정보 가져오기
        List<ZoneDTO> zoneDTOList = getZones();

        //구역 별 남은 좌석 수 저장
//        Map<ZoneDTO, Integer> zoneMap = new LinkedHashMap<>();
        Map<Integer, ZoneDTO> zoneInfo = new LinkedHashMap<>();
        Map<Integer, List<String>> zoneDetail = new LinkedHashMap<>();

        //팔린 좌석들 가져오기
        for(ZoneDTO zoneDTO : zoneDTOList){
            List<String> seats = getSoldSeats(new SoldSeatsReqDTO(gamePk, zoneDTO.getZonePk()));
            zoneDTO.setRemainingNum(zoneDTO.getTotalNum() - seats.size());
            zoneInfo.put(zoneDTO.getZonePk(), zoneDTO);
            zoneDetail.put(zoneDTO.getZonePk(), seats);
        }

        //구역 당 남은 좌석 수 세션에 저장
        session.setAttribute("zoneMap", zoneDTOList);
        session.setAttribute("zoneInfo", new ObjectMapper().writeValueAsString(zoneInfo));

        //구역 당 상세 정보 세션에 저장
        session.setAttribute("zoneMapDetail", new ObjectMapper().writeValueAsString(zoneDetail));

        return true;
    }
}
