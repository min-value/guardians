package org.baseball.domain.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.baseball.dto.UserDTO;
import org.baseball.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 로그인 페이지
    @RequestMapping("/login")
    public String showLoginPage() {
        return "user/login";
    }

    // 회원가입 페이지
    @RequestMapping("/signup")
    public String showSignupPage() {
        return "/user/signup";
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String registerUser(@ModelAttribute UserDTO user, Model model) {
        try {
            userService.registerUser(user);
            // 가입 성공 시 로그인 페이지로 이동
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            // 예외 처리(비밀번호 유효성 검사 실패 등)
            model.addAttribute("errorMessage", e.getMessage());
            return "user/signup";
        } catch (Exception e) {
            // 기타 에러 처리
            model.addAttribute("errorMessage", "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
            return "user/signup";
        }
    }

    // 마이페이지
    @RequestMapping("/mypage")
    public String showMypage() {
        return "user/mypage";
    }
}
