package org.baseball.domain.tickets;

import org.baseball.dto.PredictInfoDTO;
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

    @GetMapping("/all")
    public String showAllHomeGameList(
            @RequestParam(required = false, defaultValue = "0") String teamStatus,
            @RequestParam(required = false, defaultValue = "0") String ticketStatus,
            Model model) {

        model.addAttribute("selectedTeamStatus", teamStatus);
        model.addAttribute("selectedTicketStatus", ticketStatus);
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
}
