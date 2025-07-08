package org.baseball.domain.point;

import org.baseball.dto.PointDTO;
import org.baseball.dto.UserDTO;
import org.baseball.domain.point.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/point")
public class PointController {

    private final PointService pointService;

    @Autowired
    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    // 포인트 내역 반환
    @GetMapping("/list")
    public List<PointDTO> getUserPoints(HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("loginUser");
        if (user == null) return Collections.emptyList();

        return pointService.getPointsByUserPk(user.getUserPk());
    }

    // 포인트 적립
    @PutMapping("/plus")
    public ResponseEntity<Void> plusPoint(@RequestBody PointDTO dto) {
        pointService.addPoint(dto);
        return ResponseEntity.ok().build();
    }

    // 포인트 차감
    @PutMapping("/minus")
    public ResponseEntity<Void> minusPoint(@RequestBody PointDTO dto) {
        pointService.addPoint(dto);
        return ResponseEntity.ok().build();
    }

}
