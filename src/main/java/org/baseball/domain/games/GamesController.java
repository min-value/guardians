package org.baseball.domain.games;

import org.baseball.dto.GamedetailsDTO;
import org.baseball.dto.RankDTO;
import org.baseball.dto.ScheduleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.List;

@Controller
public class GamesController {

    private final GamesService gamesService;

    @Autowired
    public GamesController(GamesService gamesService) {
        this.gamesService = gamesService;
    }

    // 팀 랭킹 페이지
    @GetMapping("/games/rank")
    public String showRank(Model model) {
        List<RankDTO> teamList = gamesService.getTeamRanking();
        model.addAttribute("teamList", teamList);
        return "games/rank";
    }

    // 경기 일정 페이지
    @GetMapping("/games/all")
    public String showSchedulePage() {
        return "games/schedule";
    }

    // 특정 월 경기 일정 조회용
    @GetMapping("/games/schedule")
    @ResponseBody
    public List<ScheduleDTO> getScheduleMonth(@RequestParam int year, @RequestParam int month) {
        return gamesService.getScheduleMonth(year, month);
    }

    // 상세 결과 페이지
    @GetMapping("/games/details")
    public String showDetails(Model model) {
        LocalDate now = LocalDate.now(); // 오늘 날짜 기준
        int year = now.getYear();
        int month = now.getMonthValue();

        List<GamedetailsDTO> gameList = gamesService.getGameDetails(year, month);

        model.addAttribute("detailList", gameList);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedMonth", month);

        return "games/details";
    }

    // 월별 필터링
    @GetMapping("/games/details/filter")
    @ResponseBody
    public List<GamedetailsDTO> getDetailsByMonth(@RequestParam int year, @RequestParam int month) {
        return gamesService.getGameDetails(year, month);
    }
}
