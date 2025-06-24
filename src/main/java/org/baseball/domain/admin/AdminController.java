package org.baseball.domain.admin;

import lombok.extern.slf4j.Slf4j;
import org.baseball.dto.AddGameInfoDTO;
import org.baseball.dto.GamesInfoDTO;
import org.baseball.dto.HomeGameDTO;
import org.baseball.dto.ReserveInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class AdminController {
    @Autowired
    AdminService adminService;

    @GetMapping("/admin/home")
    public String home(Model model){
        return "admin/main";
    }

    @GetMapping("/admin/tickets/reservation/page")
    @ResponseBody
    public Map<String, Object> showReservationList(@RequestParam(defaultValue = "1") int page) {
        List<ReserveInfoDTO> list = adminService.showReservationList(page);
        int totalCount = adminService.countReservation();

        Map<String, Object> response = new HashMap<>();
        response.put("list", list);
        response.put("totalCount", totalCount);
        return response;
    }

    @GetMapping("/admin/tickets/reservation")
    public String checkReservation(Model model) {
        return "admin/checkReservation";
    }

    @GetMapping("/admin/games/allgame")
    public String checkGamesList() {
        return "/admin/gamesList";
    }

    @GetMapping("/admin/games/allgame/page")
    @ResponseBody
    public Map<String, Object> showGamesList(
            @RequestParam(value = "status", required = false, defaultValue = "0") String status,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            Model model) {
        List<GamesInfoDTO> list = adminService.showGamesList(status, page);
        int totalCount = adminService.countGames(status);

        Map<String, Object> response = new HashMap<>();
        response.put("list", list);
        response.put("totalCount", totalCount);
        model.addAttribute("selectedStatus", status);
        return response;
    }

    @GetMapping("/admin/tickets/addgame")
    public String checkGamesAddList() {
        return "/admin/addGames";
    }

    @GetMapping("/admin/tickets/addgame/page")
    @ResponseBody
    public Map<String, Object> showGamesAddList(@RequestParam(defaultValue = "1") int page) {
        List<GamesInfoDTO> list = adminService.showGamesAddList(page);
        int totalCount = adminService.countHomeGames();

        Map<String, Object> response = new HashMap<>();
        response.put("list", list);
        response.put("totalCount", totalCount);
        return response;
    }

    @GetMapping("/admin/tickets/gameinfo")
    @ResponseBody
    public AddGameInfoDTO showAddGameInfo(@RequestParam("gameNo") int gameNo) {
        return adminService.showAddGameInfo(gameNo);
    }

    @PostMapping("/admin/tickets/addgame")
    @ResponseBody
    public boolean addGame(@RequestBody HomeGameDTO homeGameDTO) {
        return adminService.addHomeGame(homeGameDTO);
    }
}
