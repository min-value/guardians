package org.baseball.domain.reservation;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.baseball.domain.redis.QueueService;
import org.baseball.domain.redis.RedisService;
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
    private QueueService queueService;

    //등급 및 좌석 선택 페이지 로드
    @GetMapping("/seat")
    public String seat(@RequestParam("gamePk") int gamePk, HttpSession session, Model model) throws JsonProcessingException {
        //게임 정보 세션에 저장
        session.setAttribute("gamePk", gamePk);

        //구역 정보 세션에 저장
        model.addAttribute("zoneMap", reservationService.getZones(gamePk));

        //경기 정보 세션에 저장
        ReserveGameInfoDTO reserveGameInfoDTO = reservationService.getGameInfo(gamePk);
        session.setAttribute("gameInfo", reserveGameInfoDTO);

        return "reservation/tickets1";
    }

    //seat 페이지 최초 로드 시 정보 불러오기
    @GetMapping("/seat/load")
    @ResponseBody
    public SeatLoadResDTO seatLoad(@RequestParam int gamePk,
                                   @RequestParam("check") int check,
                                   @RequestParam(value = "seats", required = false)  List<String> seats,
                                   @RequestParam(value = "zonePk", required = false) Integer zonePk,
                                   @RequestParam(value = "reservelistPk") int reservelistPk,
                                   HttpSession session) throws JsonProcessingException {
        //유저 정보 가져오기
        UserDTO user = (UserDTO) session.getAttribute("loginUser");

        return reservationService.seatLoad(gamePk, check, seats, zonePk, user, reservelistPk, session);
    }

    //전체 구역 정보 불러오기
    @GetMapping("/info")
    @ResponseBody
    public Map<String, Object> getGameInfo(@RequestParam("gamePk") int gamePk, HttpSession session) throws JsonProcessingException {
        Map<String, Object> result = new HashMap<>();
        reservationService.getSeatsInfo(gamePk, result);
        return result;
    }

    /*
    0: 미로그인 상태
    1: 정상(대기열 ttl + 선점 ttl 정상)
    2: 좌석 선점 종료
    3: 대기열 ttl 종료
     */
    //대기열 ttl + 선점 확인
    @PostMapping("/preemption/confirm")
    @ResponseBody
    public int confirmPreempt(@RequestBody PreemptConfirmReqDTO preemptConfirmReqDTO, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("loginUser");

        if(user == null) {
            return 0; //로그인 X
        }

        //대기열 ttl 확인
        if(!queueService.checkTTL(preemptConfirmReqDTO.getGamePk(), user.getUserPk())) {
            return 3;
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
            result.setPreempted(0);
            result.setErrorMsg("로그인이 필요합니다.");

            return result;
        }

        //대기열 ttl 확인
        if(!queueService.checkTTL(preemptionDTO.getGamePk(), user.getUserPk())) {
            PreemptionResDTO result = new PreemptionResDTO();
            result.setPreempted(4);
            result.setErrorMsg("다시 접속해주세요.");
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

    @DeleteMapping("/preemption/delete/auto")
    @ResponseBody
    public void deletePreemptionAuto(@RequestParam int gamePk, @RequestParam int zonePk, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("loginUser");
        //Redis 삭제
        reservationService.deletePreemptionAuto(gamePk, zonePk, user);
    }

    //권종 및 할인 선택 페이지 로드
    @GetMapping("/discount")
    public String discount(@RequestParam int gamePk, HttpSession session) {
        session.setAttribute("gamePk", gamePk);
        return "reservation/tickets2";
    }

    //예매 확인 페이지 로드
    @GetMapping("/confirm")
    public String confirm(@RequestParam int gamePk, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("loginUser");

        if(user == null) { return "redirect:/errors/needLogin"; }

        session.setAttribute("gamePk", gamePk);
        return "reservation/tickets3";
    }

    @GetMapping("/errors/needLogin")
    public String needLogin() {return "/reservation/errors/needLogin";}

    //세션 삭제
    @PostMapping("/session/clear")
    @ResponseBody
    public void clearSession(HttpSession session) {
        session.removeAttribute("gamePk");
        session.removeAttribute("gameInfo");
        session.removeAttribute("zoneMap");
        session.removeAttribute("available");
    }

    //대기열 ttl 존재 여부 확인
    @GetMapping("/queue/confirm")
    @ResponseBody
    public boolean confirmQueue(@RequestParam int gamePk, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("loginUser");

        return queueService.checkTTL(gamePk, user.getUserPk());
    }
}
