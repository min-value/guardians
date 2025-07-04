package org.baseball.domain.point;

import org.baseball.domain.user.UserMapper;
import org.baseball.dto.PointDTO;
import org.baseball.domain.point.PointMapper;
import org.baseball.dto.PointEarnDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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

    @Transactional
    @Override
    public void earnPredictionPoints() {
        List<PointEarnDTO> targetList = pointMapper.getPointEarnTargetList();

        for (PointEarnDTO dto : targetList) {
            int point = 0;
            String description = "";
            String result = dto.getResult();
            int predict = dto.getPredict();
            int paidAmount = dto.getPaidAmount();

            // 포인트 계산
            if ("DRAW".equals(result)) {
                point = (int) (paidAmount * 0.03);
                description = "무승부 3%";
            } else if (
                    ("WIN".equals(result) && predict == 0) ||
                            ("LOSE".equals(result) && predict == 1)
            ) {
                point = (int) (paidAmount * 0.05);
                description = "예측 성공 5%";
            } else {
                point = (int) (paidAmount * 0.01);
                description = "예측 실패 1%";
            }

            // 포인트 적립 내역 생성
            PointDTO pointDTO = new PointDTO();
            pointDTO.setUserPk(dto.getUserPk());
            pointDTO.setPoint(point);
            pointDTO.setDate(new Timestamp(System.currentTimeMillis()));
            pointDTO.setType("직관 예측 포인트 적립");
            pointDTO.setDescription(description);

            // 포인트 저장 + 유저 포인트 업데이트
            pointMapper.insertPoint(pointDTO);
            userMapper.updateTotalPoint(dto.getUserPk(), point);


            // 해당 예약 is_earn = 1 처리
            pointMapper.updateIsEarn(dto.getReservelistPk());
        }
    }
}
