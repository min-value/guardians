package org.baseball.domain.community;

import org.baseball.dto.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/community")
public class CommunityController {

    @Autowired
    private CommunityService communityService;
    @Autowired
    private CommunityMapper communityMapper;

    @GetMapping("/post")
    public String community(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            Model model, PostDto postDto) {
        Map<String, Object> list = communityService.getPostList(type, keyword
        );
        model.addAttribute("list", list.get("list"));
        model.addAttribute("count", list.get("count"));
        return "community/community";
    }

    @GetMapping("/page")
    @ResponseBody
    public Map<String, Object> getCommunityPage(@RequestParam(defaultValue = "1") int page){
        int size = 10;
        int offset= (page - 1) * size;

        Map<String, Object> param = new HashMap<>();
        param.put("offset", offset);
        param.put("size", size);

        List<PostDto> list = communityMapper.getPostPage(param);
        int count = communityMapper.countAll();

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("totalCount", count);
        return result;
    }
}
