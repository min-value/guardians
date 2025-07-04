package org.baseball.domain.tickets;

import org.baseball.domain.redis.RedisService;
import org.baseball.dto.PredictInfoDTO;
import org.baseball.dto.PurchaseReqDTO;
import org.baseball.dto.TicketsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tickets")
public class TicketsController {
    @Autowired
    TicketsService ticketsService;

    @Autowired
    RedisService redisService;

    @GetMapping("/all")
    public String showAllHomeGameList(
            @RequestParam(required = false, defaultValue = "0") String teamStatus,
            @RequestParam(required = false, defaultValue = "0") String ticketStatus,
            @RequestParam(required = false) String showModal,
            Model model) {

        model.addAttribute("selectedTeamStatus", teamStatus);
        model.addAttribute("selectedTicketStatus", ticketStatus);
        model.addAttribute("showModal", showModal); // JSP로 넘김
        return "tickets/homeGameList";
    }

    @GetMapping("/allgames")
    @ResponseBody
    public Map<String, Object>  showAllGamesList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) int teamStatus,
            @RequestParam(required = false) int ticketStatus) {

        List<TicketsDTO> list = ticketsService.getTicketsList(page, teamStatus, ticketStatus);
        int totalCount = ticketsService.countTicketsList(teamStatus, ticketStatus);

        Map<String, Object> response = new HashMap<>();
        response.put("list", list);
        response.put("totalCount", totalCount);
        return response;
    }

    @PostMapping("/predict")
    @ResponseBody
    public int predict(@RequestParam int userPk,
                       @RequestParam int reservelistPk,
                       @RequestParam int predict) {
        return ticketsService.updatePredict(userPk, reservelistPk, predict);
    }

    @GetMapping("/predict")
    @ResponseBody
    public PredictInfoDTO getGameInfoforPredict(@RequestParam int gamePk) {
        return ticketsService.getGameInfoforPredict(gamePk);
    }

    @PostMapping("/purchase")
    @ResponseBody
    public boolean updatePurchase(@RequestBody PurchaseReqDTO dto) {
        try {
            boolean r = redisService.confirmPayment(
                    dto.getGame_pk(),
                    dto.getSeats(),
                    dto.getUser_pk(),
                    dto.getZone_pk()
            );

            return r && ticketsService.updatePurchase(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping("/queue")
    public  String showQueueList(Model model) {
        return "tickets/queueModal";
    }
}
