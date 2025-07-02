package org.baseball.domain.reservation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.baseball.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/reservation")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    //등급 및 좌석 선택 페이지 로드
    @GetMapping("/seat")
    public String seat(@RequestParam("gamePk") int gamePk, HttpSession session) throws JsonProcessingException {
        //경기 정보 가져오기(상대 팀 + 날짜)
        ReserveGameInfoDTO reserveGameInfoDTO = reservationService.getGameInfo(gamePk);
        //게임 정보 세션에 저장
        session.setAttribute("gameInfo", reserveGameInfoDTO);
        session.setAttribute("gameInfoJson", new ObjectMapper().writeValueAsString(reserveGameInfoDTO));

        //좌석 정보 세션에 저장
        reservationService.getSeatInfo(gamePk, session);

        //할인 정보 세션에 저장
        List<DiscountDTO> discountDTOList = reservationService.getDiscountInfo();
        session.setAttribute("discountInfo", new ObjectMapper().writeValueAsString(discountDTOList));

        return "reservation/tickets1";
    }

    //전체 구역 정보 불러오기
    @GetMapping("/info")
    @ResponseBody
    public Map<String, Object> getGameInfo(@RequestParam("gamePk") int gamePk, HttpSession session) throws JsonProcessingException {
        reservationService.getSeatInfo(gamePk, session);

        Map<String, Object> result = new HashMap<>();
        result.put("zoneMap", session.getAttribute("zoneMap"));
        result.put("zoneInfo", session.getAttribute("zoneInfo"));
        result.put("zoneMapDetail", session.getAttribute("zoneMapDetail"));
        return result;
    }

    //해당 좌석 상태 확인 및 선점
    @PostMapping("/preemption/add")
    @ResponseBody
    public PreemptionResDTO preemptSeat(@RequestBody PreemptionDTO preemptionDTO, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("loginUser");
        try {
            return reservationService.preemptSeat(preemptionDTO, user);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //선점 해제
    @DeleteMapping("/preemption/delete")
    @ResponseBody
    public boolean deletePreemption(@RequestParam int reservelistPk) {
        try {
            reservationService.deletePreemption(reservelistPk);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //권종 및 할인 선택 페이지 로드
    @GetMapping("/discount")
    public String discount() {
        return "reservation/tickets2";
    }

    //예매 확인 페이지 로드
    @GetMapping("/confirm")
    public String confirm() {
        return "reservation/tickets3";
    }

    @GetMapping("/errors/needLogin")
    public String needLogin() {return "/reservation/errors/needLogin";}
}
