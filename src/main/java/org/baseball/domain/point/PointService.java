package org.baseball.domain.point;

import org.baseball.dto.PointDTO;
import java.util.List;

public interface PointService {
    List<PointDTO> getPointsByUserPk(int userPk);

    // 포인트 금액 합산
    int calculateTotalPoints(List<PointDTO> points);
}