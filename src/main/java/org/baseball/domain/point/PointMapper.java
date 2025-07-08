package org.baseball.domain.point;

import org.apache.ibatis.annotations.Mapper;
import org.baseball.dto.PointDTO;
import org.baseball.dto.PointEarnDTO;

import java.util.List;
import java.util.Map;

@Mapper
public interface PointMapper {
    List<PointDTO> getPointsByUserPk(int userPk);
    void insertPoint(PointDTO pointDTO);
    void insertPointReward(Map<String, Object> param);
    void updateTotalPoint(Map<String, Object> param);

    List<PointEarnDTO> getPointEarnTargetList();
    void updateIsEarn(int reservelistPk);
}
