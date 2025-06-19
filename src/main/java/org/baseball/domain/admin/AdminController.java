package org.baseball.domain.admin;

import lombok.extern.slf4j.Slf4j;
import org.baseball.dto.ReserveInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/admin/reservation")
    public String checkReservation(Model model, ReserveInfoDto reserveInfoDTO) {
        List<ReserveInfoDto> list = adminService.showReserveList();
        log.info("check!");
        model.addAttribute("list", list);
        return "admin/check-reservation";
    }

    @GetMapping("/admin/gamelist")
    public String showGameList(Model model){
        return "admin/games-list";
    }

    @GetMapping("/admin/addgames")
    public String addGames(Model model){
        return "admin/add-games";
    }

}
