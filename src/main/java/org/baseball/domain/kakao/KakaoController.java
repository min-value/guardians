package org.baseball.domain.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.ui.Model;
import org.baseball.domain.user.UserService;
import org.baseball.dto.UserDTO;
import org.baseball.domain.kakao.Tokens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/kakao")
public class KakaoController {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Autowired
    private KakaoService kakaoService;
    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    public String kakaoCallback(@RequestParam String code, HttpSession session) {
        try {
            // 1) code → access/refresh token
            Tokens tokens = kakaoService.getTokens(code);
            System.out.println("▶ accessToken: " + tokens.getAccessToken());
            System.out.println("▶ refreshToken: " + tokens.getRefreshToken());

            // 2) 프로필 조회
            JsonNode profile = kakaoService.getProfile(tokens.getAccessToken());
            System.out.println("▶ profile: " + profile);

            String kakaoIdStr = profile.get("id").asText();

            // 3) 기존/신규 분기
            UserDTO exist = userService.findByKakaoToken(kakaoIdStr);
            if (exist != null) {
                session.setAttribute("loginUser", exist);
                return "redirect:/home";
            }

            UserDTO newUser = userService.loginOrRegisterKakao(profile, tokens);
            session.setAttribute("loginUser", newUser);
            return "redirect:/user/kakao/signup";

        } catch (Exception e) {
            e.printStackTrace();
            return "error/500";
        }
    }

    @GetMapping("/signup")
    public String showKakaoSignupForm(HttpSession session, Model model) {
        UserDTO kakaoUser = (UserDTO) session.getAttribute("loginUser");
        model.addAttribute("user", kakaoUser);
        return "kakao/signup";
    }

    @PostMapping("/complete")
    public String completeKakaoSignup(
            @RequestParam String email,
            @RequestParam String tel,
            HttpSession session) {

        UserDTO kakaoUser = (UserDTO) session.getAttribute("loginUser");
        if (kakaoUser == null) {
            return "redirect:/login";
        }

        userService.updateKakaoUserInfo(
                kakaoUser.getUserPk(),
                email,
                tel,
                kakaoUser.getAccessToken(),
                kakaoUser.getRefreshToken()
        );

        // 사용자 정보 업데이트
        kakaoUser.setEmail(email);
        kakaoUser.setTel(tel);
        session.setAttribute("loginUser", kakaoUser);

        return "redirect:/home";
    }
}
