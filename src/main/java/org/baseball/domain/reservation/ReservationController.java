package org.baseball.domain.reservation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
    @GetMapping("/seat")
    public String seat() {
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
