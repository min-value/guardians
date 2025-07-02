package org.baseball.domain.user;

import javax.servlet.http.HttpSession;

import org.baseball.domain.myfairy.MyFairyService;
import org.baseball.domain.myticket.MyTicketService;
import org.baseball.domain.tickets.TicketsService;
import org.baseball.dto.MyFairyDTO;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.baseball.dto.UserDTO;
import org.baseball.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    private final UserService userService;
    private final MyFairyService myFairyService;
    private final MyTicketService myTicketService;
    private final TicketsService ticketsService;

    @Autowired
    public UserController(UserService userService, MyFairyService myFairyService, MyTicketService myTicketService, TicketsService ticketsService) {
        this.userService = userService;
        this.myFairyService = myFairyService;
        this.myTicketService = myTicketService;
        this.ticketsService = ticketsService;
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
            return "redirect:/home";
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
    @RequestMapping("/user/signup/check")
    @ResponseBody
    public boolean checkUser(String userId){
        return userService.checkUserId(userId);
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

    // 마이페이지 탭별 컨텐츠 반환 (AJAX용)
    @GetMapping("/user/mypage/info")
    public String getInfoTab(HttpSession session, Model model) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "user/login";
        }
        model.addAttribute("user", loginUser);
        return "user/mypage/info";
    }

    @GetMapping("/user/mypage/tickets")
    public String getTicketsTab() {
        return "user/mypage/tickets";
    }

    @GetMapping("/user/mypage/points")
    public String getPointsTab() {
        return "user/mypage/points";
    }

    @GetMapping("/user/mypage/fairy")
    public String getFairyTab() {
        return "user/mypage/fairy";
    }

    // 마이페이지 내 정보 수정
    @PostMapping("/user/update")
    @ResponseBody
    public Map<String, Object> updateUserAjax(@ModelAttribute UserDTO user, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            result.put("success", false);
            result.put("message", "로그인이 필요합니다.");
            return result;
        }

        //  기존 로그인 유저의 ID로 강제 고정, ID는 수정 불가능
        user.setUserId(loginUser.getUserId());

        try {
            userService.updateUserInfo(user);
            session.setAttribute("loginUser", user);

            result.put("success", true);
            result.put("message", "회원정보가 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "회원정보 수정 중 오류가 발생했습니다.");
        }
        return result;
    }

    // 마이페이지 예매내역
    @GetMapping("/user/tickets")
    @ResponseBody
    public List<Map<String, Object>> getTicketsList(HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return new ArrayList<>();
        }

        int userPk = loginUser.getUserPk();
        return myTicketService.getTicketsByUserPk(userPk);
    }

    // 마이페이지 포인트내역 조회
    @GetMapping("/user/point/total")
    @ResponseBody
    public int getUserTotalPoint(HttpSession session) {

        UserDTO user = (UserDTO) session.getAttribute("loginUser");
        if (user == null) return 0;

        return userService.getTotalPoint(user.getUserPk());
    }
    
    // 마이페이지 승리요정 조회
    @GetMapping("/user/fairy/data")
    @ResponseBody
    public MyFairyDTO getFairyData(HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return new MyFairyDTO();
        }
        try {
            MyFairyDTO result = myFairyService.getMyFairyInfo(loginUser.getUserPk());
            System.out.println(">>> MyFairyDTO result: " + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new MyFairyDTO();
        }
    }

    @PostMapping("/user/cancel")
    @ResponseBody
    @Transactional
    public boolean cancelReservation(@RequestParam int reservelist_pk,
                                     @RequestParam int user_pk,
                                     @RequestParam int point) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_pk", user_pk);
        map.put("point", point);
        int updated1 = myTicketService.cancelReservationList(reservelist_pk);
        int updated2 = myTicketService.cancelReservations(reservelist_pk);
        int updated3 = myTicketService.insertRefundPoint(map);
        int updated4 = myTicketService.updateRefundPoint(map);

        return updated1>0 && updated2>0 && updated3>0 && updated4>0;
    }

}
