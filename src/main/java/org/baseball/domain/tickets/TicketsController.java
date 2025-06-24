package org.baseball.domain.tickets;

import org.baseball.dto.GamesInfoDTO;
import org.baseball.dto.TicketsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
        return "tickets/stadium";
    }

    @GetMapping("/test2")
    public String test2() {
        return "tickets/seats";
    }

    @GetMapping("/test3")
    public String test3() {
        return "tickets/tickets1";
    }

    @GetMapping("/test4")
    public String test4() {
        return "tickets/tickets2";
    }

    @GetMapping("/test5")
    public String test5() {
        return "tickets/tickets3";
    }

    @GetMapping("/all")
    public String showAllHomeGameList() {
        return "tickets/HomeGameList";
    }

    @GetMapping("/allgames")
    @ResponseBody
    public Map<String, Object>  showAllGamesList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String teamStatus,
            @RequestParam(required = false) String ticketStatus) {

        List<TicketsDTO> list = ticketsService.getTicketsList(page, teamStatus, ticketStatus);
        int totalCount = ticketsService.countTicketsList();

        Map<String, Object> response = new HashMap<>();
        response.put("list", list);
        response.put("totalCount", totalCount);
        return response;
    }
}
