package org.baseball;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {
    @GetMapping("/home")
    public String home(Model model) {
        System.out.println("sout");
        log.debug("debug");
        log.trace("trace");
        log.info("index");
        return "home";
    }
}