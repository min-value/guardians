package org.baseball.domain.user;

import javax.servlet.http.HttpSession;
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
    @RequestMapping("/user/login")
    public String showLoginPage() {
        return "user/login";
    }

    // 로그인 처리
    @PostMapping("/user/login")
    public String login(@RequestParam String userId,
                        @RequestParam String userPwd,
                        HttpSession session,
                        Model model) {
        System.out.println("로그인 시도: " + userId);
        UserDTO loginUser = userService.login(userId, userPwd);

        if (loginUser != null) {
            System.out.println("로그인 성공: " + loginUser.getUserName());
            session.setAttribute("loginUser", loginUser);
            return "redirect:/user/mypage";
        } else {
            System.out.println("로그인 실패");
            model.addAttribute("errorMessage", "아이디 또는 비밀번호가 틀렸습니다.");
            return "user/login";
        }
    }

    // 로그아웃 처리
    @RequestMapping("/user/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 로그인 정보 삭제
        return "redirect:/home";
    }

    // 회원가입 페이지
    @RequestMapping("/user/signup")
    public String showSignupPage() {
        return "user/signup";
    }

    // 회원가입 처리
    @PostMapping("/user/signup")
    public String registerUser(@ModelAttribute UserDTO user, Model model) {
        try {
            userService.registerUser(user);
            // 가입 성공 시 로그인 페이지로 이동
            return "redirect:/user/login";
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
    @RequestMapping("/user/mypage")
    public String showMypage(HttpSession session, Model model) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser != null) {
            model.addAttribute("user", loginUser);
        }
        return "user/mypage";
    }

    // 마이페이지 내 정보 수정
    @PostMapping("/user/update")
    public String updateUser(@ModelAttribute UserDTO user, HttpSession session, Model model) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/user/login";
        }

        // 기존 로그인 유저의 ID로 강제 고정 Id는 수정 불가능
        user.setUserId(loginUser.getUserId());

        try {
            userService.updateUserInfo(user);
            session.setAttribute("loginUser", user);
            return "redirect:/user/mypage";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "회원정보 수정 중 오류 발생");
            return "user/mypage";
        }
    }
}
