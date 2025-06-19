package org.baseball.domain.community;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommunityController {
    @GetMapping("/community.do")
    public String home(Model model) {
        System.out.println("sout");
        return "community/community";
    }
}
