package org.baseball.domain.numball;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.baseball.dto.NumballDTO;
import org.baseball.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/point")
public class NumBallController {

    private final NumballService numballService;

    @Autowired
    public NumBallController(NumballService numballService) {
        this.numballService = numballService;
    }

    // 숫자야구 JSP 페이지 렌더링
    @GetMapping("/numball")
    public String showNumBallPage(HttpSession session, HttpServletRequest request) {
        UserDTO user = (UserDTO) session.getAttribute("loginUser");
        if (user != null) {
            numballService.getOrCreateTodayGame(user.getUserPk());
            NumballDTO dto = numballService.getOrCreateTodayGame(user.getUserPk());
            boolean isSuccess = dto.getIsSuccess() == 1;
            request.setAttribute("isSuccess", isSuccess);
        }
        return "numball/numball";
    }

    // 숫자야구 확인 버튼 클릭
    @PostMapping("/numball/check")
    @ResponseBody
    public Map<String, Object> checkAnswer(@RequestParam("input") String input, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        UserDTO user = (UserDTO) session.getAttribute("loginUser");
        if (user == null) {
            response.put("status", "unauthorized");
            return response;
        }

        NumballDTO numball = numballService.getOrCreateTodayGame(user.getUserPk());

        if (numball.getTryCount() >= 6 || numball.getIsSuccess() == 1) {
            response.put("status", "over");
            return response;
        }

        numballService.increaseTryCount(numball.getNumballPk());

        String answer = numball.getAnswer();
        int strike = 0;
        int ball = 0;

        for (int i = 0; i < 3; i++) {
            char userChar = input.charAt(i);
            if (userChar == answer.charAt(i)) {
                strike++;
            } else if (answer.contains(String.valueOf(userChar))) {
                ball++;
            }
        }

        if (strike == 3) {
            numballService.setSuccess(numball.getNumballPk());
            response.put("status", "success");
        } else {
            response.put("status", "fail");
        }

        response.put("strike", strike);
        response.put("ball", ball);

        return response;
    }

    @PostMapping("/numball/try")
    @ResponseBody
    public void saveTry(@RequestBody Map<String, Object> data, HttpSession session) {
        try {
            UserDTO user = (UserDTO) session.getAttribute("loginUser");
            if (user == null) return;

            NumballDTO numball = numballService.getOrCreateTodayGame(user.getUserPk());

            ObjectMapper mapper = new ObjectMapper();
            String triesJson = mapper.writeValueAsString(data.get("tries"));
            numballService.updateTries(numball.getNumballPk(), triesJson);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/numball/tries")
    @ResponseBody
    public Map<String, Object> loadTries(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        UserDTO user = (UserDTO) session.getAttribute("loginUser");
        if (user == null) return result;

        NumballDTO numball = numballService.getOrCreateTodayGame(user.getUserPk());
        String triesJson = numballService.getTries(numball.getNumballPk());

        result.put("tries", triesJson);
        result.put("isSuccess", numball.getIsSuccess() == 1);
        result.put("tryCount", numball.getTryCount());
        return result;
    }
}
