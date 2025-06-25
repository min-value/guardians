package org.baseball.domain.fairy;

import lombok.extern.slf4j.Slf4j;
import org.baseball.dto.FairyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
public class FairyController {
    @Autowired
    FairyService fairyService;

    @GetMapping("/fairy")
    public String fairyMain(Model model) {
        List<FairyDTO> list = fairyService.showFairyRank();
        model.addAttribute("list", list);
        return "/fairy/fairy";
    }


}
