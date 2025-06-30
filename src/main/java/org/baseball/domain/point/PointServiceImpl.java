package org.baseball.domain.point;

import org.baseball.domain.user.UserMapper;
import org.baseball.dto.PointDTO;
import org.baseball.domain.point.PointMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PointServiceImpl implements PointService {

    private final PointMapper pointMapper;
    private final UserMapper userMapper;

    @Autowired
    public PointServiceImpl(PointMapper pointMapper, UserMapper userMapper) {
        this.pointMapper = pointMapper;
        this.userMapper = userMapper;
    }

    @Override
    public List<PointDTO> getPointsByUserPk(int userPk) {
        return pointMapper.getPointsByUserPk(userPk);
    }

    @Transactional
    public void addPoint(PointDTO pointDTO) {
        // 포인트 내역 저장
        pointMapper.insertPoint(pointDTO);

        // 유저 포인트 총합 업데이트
        userMapper.updateTotalPoint(pointDTO.getUserPk(), pointDTO.getPoint());
    }
}
