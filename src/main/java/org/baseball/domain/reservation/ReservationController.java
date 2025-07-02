package org.baseball.domain.reservation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.baseball.domain.Redis.RedisService;
import org.baseball.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Autowired
    private RedisService redisService;
    @Autowired
    private ReservationMapper reservationMapper;

    //등급 및 좌석 선택 페이지 로드
    @GetMapping("/seat")
    public String seat(@RequestParam("gamePk") int gamePk, HttpSession session, Model model) throws JsonProcessingException {
        //로그인 확인
        try {
        UserDTO user = (UserDTO) session.getAttribute("loginUser");


        if(user == null) {
            return "reservation/errors/needLogin";
        }

        //해당 유저가 해당 게임에 구매한 좌석이 4개 이하인지 확인
        int cnt = reservationService.countUserReserve(gamePk, user.getUserPk());
        if(cnt >= 4) {
            model.addAttribute("errorMsg", "최대 4장까지만 구매가 가능합니다.");
            return "reservation/errors/generalError";
        }

        //남은 개수 세션에 저장
        session.setAttribute("available", 4 - cnt);

        //우리팀 경기인지 확인
        if(!reservationService.isOurGame(gamePk)) {
            model.addAttribute("errorMsg", "존재하지 않는 경기입니다.");
            return "reservation/errors/generalError";
        }

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
        } catch(Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMsg", "내부 서버 오류");
            return "reservation/errors/generalError";
        }
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

    @PostMapping("/preemption/confirm")
    @ResponseBody
    public int confirmPreempt(@RequestBody PreemptConfirmReqDTO preemptConfirmReqDTO, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("loginUser");

        if(user == null) {
            return 0; //로그인 X
        }

        return reservationService.confirmPreempt(preemptConfirmReqDTO, user);
    }

    //해당 좌석 상태 확인 및 선점
    @PostMapping("/preemption/add")
    @ResponseBody
    public PreemptionResDTO preemptSeat(@RequestBody PreemptionDTO preemptionDTO, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("loginUser");
        if(user == null) {
            PreemptionResDTO result = new PreemptionResDTO();
            result = new PreemptionResDTO();
            result.setPreempted(0);
            result.setErrorMsg("로그인이 필요합니다.");

            return result;
        }

        return reservationService.preemptSeat(preemptionDTO, user);
    }

    //선점 해제
    @DeleteMapping("/preemption/delete")
    @ResponseBody
    public int deletePreemption(@RequestBody PreemptDeleteReqDTO preemptdeleteReqDTO,
                                HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("loginUser");
        if (user == null) return 0; // 로그인 X

        return reservationService.deletePreemption(preemptdeleteReqDTO, user);
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

    @GetMapping("/test")
    public String test(@RequestParam("gamePk") int gamePk, @RequestParam("zonePk") int zonePk, @RequestParam("userPk") int userPk) {
        log.info(reservationService.createReserveCode(gamePk, zonePk, userPk));
        return null;
    }
}
