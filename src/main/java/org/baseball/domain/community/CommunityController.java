package org.baseball.domain.community;

import org.baseball.dto.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @GetMapping("/community.do")
    public String home(Model model, PostDto postDto) {
        System.out.println("sout");

        model.addAttribute("count", communityService.checkCount(postDto));
        return "community/community";
    }
}
