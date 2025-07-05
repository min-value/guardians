package org.baseball.domain.reservation;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
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
    public SeatLoadResDTO seatLoad(@RequestParam int gamePk, HttpSession session) throws JsonProcessingException {
        try {
            SeatLoadResDTO result = new SeatLoadResDTO();
            result.setError(false);
            result.setResult(new HashMap<>());

            //유저 정보 가져오기
            UserDTO user = (UserDTO) session.getAttribute("loginUser");

            //우리팀 경기인지 확인
            if (!reservationService.isOurGame(gamePk)) {
                result.setError(true);
                result.setErrorMsg("존재하지 않는 경기입니다.");
                return result;
            }

            //해당 유저가 해당 게임에서 구매한 좌석이 4개 이하인지 확인
            int cnt = reservationService.countUserReserve(gamePk, user.getUserPk());

            //남은 좌석 수 저장 - available(세션)
            if (cnt >= 4) {
                result.setError(true);
                result.setErrorMsg("최대 4장까지만 구매가 가능합니다");
                return result;
            }
            int remainingCnt = 4 - cnt;
            session.setAttribute("available", remainingCnt); //세션에 저장
            result.getResult().put("available", remainingCnt);

            //경기 정보 저장 (gameInfo) - gameInfo(세션), gamePk
            ReserveGameInfoDTO reserveGameInfoDTO = reservationService.getGameInfo(gamePk);
            result.getResult().put("gameInfo", reserveGameInfoDTO);
            session.setAttribute("gameInfo", reserveGameInfoDTO); //세션에 저장(JSON으로 파싱)

            //좌석 정보 저장 - zoneInfo(구역 별 상세 정보), zoneMapDetail(구역별 팔린 좌석 목록)
            reservationService.getSeatsInfo(gamePk, result.getResult());

            //할인 정보 저장 - discountInfo
            List<DiscountDTO> discountDTOList = reservationService.getDiscountInfo();
            result.getResult().put("discountInfo", discountDTOList);

            return result;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //전체 구역 정보 불러오기
    @GetMapping("/info")
    @ResponseBody
    public Map<String, Object> getGameInfo(@RequestParam("gamePk") int gamePk, HttpSession session) throws JsonProcessingException {
        Map<String, Object> result = new HashMap<>();
        reservationService.getSeatsInfo(gamePk, result);
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

    @DeleteMapping("/preemption/delete/auto")
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

    @GetMapping("/test")
    public void test(@RequestParam int gamePk, int zonePk, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("loginUser");
        redisService.cancelPayment(gamePk, null, user.getUserPk(), zonePk);
    }
}
