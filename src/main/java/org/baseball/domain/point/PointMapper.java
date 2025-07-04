package org.baseball.domain.point;

import org.apache.ibatis.annotations.Mapper;
import org.baseball.dto.PointDTO;
import org.baseball.dto.PointEarnDTO;

import java.util.List;

@Mapper
public interface PointMapper {
    List<PointDTO> getPointsByUserPk(int userPk);
    void insertPoint(PointDTO pointDTO);

    List<PointEarnDTO> getPointEarnTargetList();
    void updateIsEarn(int reservelistPk);
}
