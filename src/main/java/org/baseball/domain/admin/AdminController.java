package org.baseball.domain.admin;

import lombok.extern.slf4j.Slf4j;
import org.baseball.dto.GamesInfoDTO;
import org.baseball.dto.ReserveInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
public class AdminController {
    @Autowired
    AdminService adminService;

    @GetMapping("/admin/home")
    public String home(Model model){
        return "admin/main";
    }

    @GetMapping("/admin/tickets/reservation")
    public String checkReservation(Model model) {
        List<ReserveInfoDTO> list = adminService.showReserveList();
        model.addAttribute("list", list);
        return "admin/check-reservation";
    }

    @GetMapping("/admin/games/allgame")
    public String showGamesList(@RequestParam(value = "status", required = false) String status, Model model) {
        List<GamesInfoDTO> list = adminService.showGamesList();
        model.addAttribute("list", list);
        model.addAttribute("selectedStatus", status); //필터링
        return "admin/games-list";
    }

    @GetMapping("/admin/tickets/addgame")
    public String showGamesAddList(Model model) {;
        List<GamesInfoDTO> list = adminService.showGamesAddList();
        model.addAttribute("list", list);
        log.info("####" + list.toString());
        return "admin/add-games";
    }

    @PostMapping("/admin/tickets/addgame")
    public String addGame(Model model){
        return "admin/add-games";
    }

}
