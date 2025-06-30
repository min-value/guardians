package org.baseball.domain.point;

import org.baseball.dto.PointDTO;
import java.util.List;

public interface PointService {
    List<PointDTO> getPointsByUserPk(int userPk);
    void addPoint(PointDTO dto);
}