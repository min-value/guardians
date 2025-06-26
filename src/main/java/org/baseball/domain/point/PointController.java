package org.baseball.domain.point;

import org.baseball.dto.PointDTO;
import org.baseball.dto.UserDTO;
import org.baseball.domain.point.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/points")
public class PointController {

    private final PointService pointService;

    @Autowired
    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping
    public Map<String, Object> getUserPoints(HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("loginUser");
        if (user == null) {
            return Collections.emptyMap();
        }

        int userPk = user.getUserPk();
        List<PointDTO> points = pointService.getPointsByUserPk(userPk);
        int totalAmount = points.stream().mapToInt(PointDTO::getPoint).sum();

        Map<String, Object> result = new HashMap<>();
        result.put("points", points);
        result.put("totalAmount", totalAmount);

        return result;
    }

}
