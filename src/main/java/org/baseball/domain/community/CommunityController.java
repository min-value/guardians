package org.baseball.domain.community;

import org.baseball.dto.CommentDTO;
import org.baseball.dto.PostDTO;
import org.baseball.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
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
        PostDTO post = communityService.getPostById(postPk);
        model.addAttribute("post", post);
        post.setP_content(post.getP_content().replace("\n", "<br/>"));
        return "community/postDetail";
    }

    @GetMapping("/post/{post_pk}/modify")
    public String showUpdate(@PathVariable("post_pk") int postPk,
                             HttpSession session,
                             Model model) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/community/post";
        }
        PostDTO post = communityService.getPostById(postPk);
        model.addAttribute("post", post);

        if(loginUser.getUserPk() == post.getUser_pk()){
            return "community/updatePost";
        }
        else{
            return "redirect:/community/post";
        }
    }

    @PostMapping("/post/{post_pk}/modify")
    public String updatePost(@PathVariable("post_pk") int postPk,
                             @ModelAttribute PostDTO modifiedPost,
                             HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        if (loginUser == null || loginUser.getUserPk() != modifiedPost.getUser_pk()) {
            return "redirect:/community/post";
        }

        modifiedPost.setPost_pk(postPk);

        communityService.modifyPost(modifiedPost);

        return "redirect:/community/post/" + postPk;
    }

    @PostMapping("/post/delete")
    public String deletePost(@RequestParam int post_pk){
        communityService.deletePost(post_pk);
        return "redirect:/community/post";
    }

    @PostMapping("/post/add")
    public String addPost(@ModelAttribute PostDTO dto){
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
    public boolean addComment(@ModelAttribute CommentDTO dto){
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

    @PostMapping("/comment/update")
    @ResponseBody
    public boolean updateComment(@ModelAttribute CommentDTO comment){
        if(comment.getC_content() == null || comment.getC_content().trim().equals("")){
            return false;
        }
        communityService.updateComment(comment);
        return true;
    }



}
