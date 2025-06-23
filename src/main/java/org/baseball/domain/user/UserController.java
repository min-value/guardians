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
    @RequestMapping("/login")
    public String showLoginPage() {
        return "user/login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String userId,
                        @RequestParam String userPwd,
                        HttpSession session,
                        Model model) {
        System.out.println("로그인 시도: " + userId);
        UserDTO loginUser = userService.login(userId, userPwd);

        if (loginUser != null) {
            System.out.println("로그인 성공: " + loginUser.getUserName());
            session.setAttribute("loginUser", loginUser);
            return "redirect:/mypage";
        } else {
            System.out.println("로그인 실패");
            model.addAttribute("errorMessage", "아이디 또는 비밀번호가 틀렸습니다.");
            return "user/login";
        }
    }

    // 로그아웃 처리
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 로그인 정보 삭제
        return "redirect:/home";
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

    // 탭별 내용 반환 (Ajax 호출용)
    @GetMapping("/mypage/info")
    public String getInfoTab(HttpSession session, Model model) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "user/mypage/empty";  // 로그인 안된 경우 빈 페이지 or 로그인 유도
        }
        // DB에서 최신 회원정보 가져와서 전달 (예시는 session 데이터 사용)
        model.addAttribute("user", loginUser);
        return "user/mypage/info";  // 내 정보 탭 JSP
    }

    @GetMapping("/mypage/tickets")
    public String getTicketsTab(HttpSession session, Model model) {
        // 예매내역 조회 로직 추가 필요
        return "user/mypage/tickets"; // 예매내역 탭 JSP
    }

    @GetMapping("/mypage/points")
    public String getPointsTab(HttpSession session, Model model) {
        // 포인트내역 조회 로직 추가 필요
        return "user/mypage/points";  // 포인트내역 탭 JSP
    }

    @GetMapping("/mypage/fairy")
    public String getFairyTab(HttpSession session, Model model) {
        // 승리요정 탭 데이터 조회 로직 추가 필요
        return "user/mypage/fairy";  // 승리요정 탭 JSP
    }
}
