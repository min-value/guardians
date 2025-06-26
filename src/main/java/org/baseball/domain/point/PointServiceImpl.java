package org.baseball.domain.point;

import org.baseball.dto.PointDTO;
import org.baseball.domain.point.PointMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointServiceImpl implements PointService {

    private final PointMapper pointMapper;

    @Autowired
    public PointServiceImpl(PointMapper pointMapper) {
        this.pointMapper = pointMapper;
    }

    @Override
    public List<PointDTO> getPointsByUserPk(int userPk) {
        return pointMapper.getPointsByUserPk(userPk);
    }

    @Override
    public int calculateTotalPoints(List<PointDTO> points) {
        return points.stream()
                .mapToInt(PointDTO::getPoint)
                .sum();
    }
}
