package org.baseball.domain.tickets;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tickets")
public class TicketsController {
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
}
