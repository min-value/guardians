package org.baseball.domain.games;

import org.baseball.dto.TeamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class GamesController {

    private final RankService rankService;

    @Autowired
    public GamesController(RankService rankService) {
        this.rankService = rankService;
    }

    // 랭킹 페이지
    @GetMapping("/games/rank")
    public String showRank(Model model) {
        List<TeamDTO> teamList = rankService.getTeamRanking();
        model.addAttribute("teamList", teamList);
        return "games/rank";
    }

    @GetMapping("/games/all")
    public String showSchedule() {
        return "games/schedule";
    }

    @GetMapping("/games/details")
    public String showDetails() {
        return "games/details";
    }
}
