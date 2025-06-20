package org.baseball.domain.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    // 로그인 페이지
    @RequestMapping("/login.do")
    public String showLoginPage() {
        return "user/login";
    }

    // 회원가입 페이지
    @RequestMapping("/signup.do")
    public String showSignupPage() {
        return "/user/signup";
    }

    // 마이페이지
    @RequestMapping("/mypage.do")
    public String showMypage() {
        return "user/mypage";
    }
}
