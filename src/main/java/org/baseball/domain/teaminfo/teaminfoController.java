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

    // 경기장 소개 페이지
    @GetMapping("/teaminfo/stadium")
    public String showStadiumPage() {
        return "teaminfo/stadium";
    }

    // 스폰서 소개 페이지
    @GetMapping("/teaminfo/sponsor")
    public String showSponsorPage() {
        return "teaminfo/sponsor";
    }

    // 선수단 소개 페이지
    @GetMapping("/teaminfo/player")
    public String showPlayerPage() {
        return "teaminfo/player";
    }
}
