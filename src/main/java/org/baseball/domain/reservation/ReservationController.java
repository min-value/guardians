package org.baseball.domain.reservation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.baseball.dto.ReserveGameInfoDTO;
import org.baseball.dto.SoldSeatsReqDTO;
import org.baseball.dto.ZoneDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/reservation")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @GetMapping("/seat")
    public String seat(@RequestParam("gamePk") int gamePk, Model model) throws JsonProcessingException {
        //경기 정보 가져오기(상대 팀 + 날짜)
        ReserveGameInfoDTO reserveGameInfoDTO = reservationService.getGameInfo(gamePk);

        //구역 정보 가져오기
        List<ZoneDTO> zoneDTOList = reservationService.getZones();

        //구역 별 남은 좌석 수 저장
        Map<ZoneDTO, Integer> zoneMap = new LinkedHashMap<>();
        Map<Integer, List<String>> zoneDetail = new LinkedHashMap<>();

        //팔린 좌석들 가져오기
        for(ZoneDTO zoneDTO : zoneDTOList){
            List<String> seats = reservationService.getSoldSeats(new SoldSeatsReqDTO(gamePk, zoneDTO.getZonePk()));
            zoneMap.put(zoneDTO, zoneDTO.getTotalNum() - seats.size());
            zoneDetail.put(zoneDTO.getZonePk(), seats);
        }


        //게임 정보 세션에 저장
        model.addAttribute("gameInfo", reserveGameInfoDTO);

        //구역 당 남은 좌석 수 세션에 저장
        model.addAttribute("zoneMap", zoneMap);

        //구역 당 상세 정보 세션에 저장
        ObjectMapper mapper = new ObjectMapper();
        String zoneMapJson = mapper.writeValueAsString(zoneDetail);

        model.addAttribute("zoneMapDetail", zoneMapJson);

        log.info(zoneMap.toString());

        return "reservation/tickets1";
    }

    @GetMapping("/discount")
    public String discount() {
        return "reservation/tickets2";
    }

    @GetMapping("/confirm")
    public String confirm() {
        return "reservation/tickets3";
    }
}
