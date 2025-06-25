package org.baseball.domain.tickets;

import org.baseball.dto.TicketsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tickets")
public class TicketsController {
    @Autowired
    TicketsService ticketsService;

    @GetMapping("/test")
    public String test() {
        return "reservation/stadium";
    }

    @GetMapping("/test2")
    public String test2() {
        return "reservation/seats";
    }

    @GetMapping("/test3")
    public String test3() {
        return "reservation/tickets1";
    }

    @GetMapping("/test4")
    public String test4() {
        return "reservation/tickets2";
    }

    @GetMapping("/test5")
    public String test5() {
        return "reservation/tickets3";
    }

    @GetMapping("/all")
    public String showAllHomeGameList(
            @RequestParam(required = false, defaultValue = "0") String teamStatus,
            @RequestParam(required = false, defaultValue = "0") String ticketStatus,
            Model model) {

        model.addAttribute("selectedTeamStatus", teamStatus);
        model.addAttribute("selectedTicketStatus", ticketStatus);
        return "tickets/HomeGameList";
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
}
