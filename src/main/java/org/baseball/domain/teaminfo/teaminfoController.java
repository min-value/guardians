package org.baseball.domain.teaminfo;

import org.baseball.dto.RankDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
@Controller
public class teaminfoController {
    // 구단소개 페이지
    @GetMapping("/teaminfo/club")
    public String showClubPage() {
        return "teaminfo/club";
    }
}
