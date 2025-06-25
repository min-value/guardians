package org.baseball.domain.community;

import org.baseball.dto.CommentDTO;
import org.baseball.dto.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/community")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @GetMapping("/page")
    @ResponseBody
    public Map<String, Object> getCommunityPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword) {
        return communityService.getPostpageWithSearch(page, type, keyword);
    }

    @GetMapping("/post")
    public String community(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            Model model) {
        Map<String, Object> result = communityService.getPostpageWithSearch(page, type, keyword);
        model.addAttribute("list", result.get("list"));
        model.addAttribute("totalcount", result.get("totalCount"));
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        return "community/community";
    }

    @GetMapping("/post/{post_pk}")
    public String post(@PathVariable("post_pk") int postPk, Model model) {
        PostDto post = communityService.getPostById(postPk);
        model.addAttribute("post", post);
        return "community/postDetail";
    }

    @PostMapping("/post/delete")
    public String deletePost(@RequestParam int post_pk){
        communityService.deletePost(post_pk);
        return "redirect:/community/post";
    }

    @PostMapping("/post/add")
    public String addPost(@ModelAttribute PostDto dto){
        communityService.addPost(dto);
        return "redirect:/community/post";
    }

    @GetMapping("/post/write")
    public String community(Model model){
        return "community/writePost";
    }
// ================< comment >===================================
    @GetMapping("/comment")
    @ResponseBody
    public Map<String, Object> getComments(
            @RequestParam(name = "post_pk") int postPk,
            @RequestParam(defaultValue = "1") int page) {
        return communityService.getCommentById(postPk, page);
    }

    @PostMapping("/comment/add")
    @ResponseBody
    public boolean addComment(@ModelAttribute CommentDTO dto, RedirectAttributes redirectAttrs){
        if(dto.getC_content() == null || dto.getC_content().trim().equals("")){
            return false;
        }
        else{
            communityService.addComment(dto);
            return true;
        }
    }

    @PostMapping("/comment/delete")
    @ResponseBody
    public boolean deleteComment(@RequestParam int comment_pk){
        communityService.deleteComment(comment_pk);
        return true;
    }



}
