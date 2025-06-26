package org.baseball.domain.point;

import org.apache.ibatis.annotations.Mapper;
import org.baseball.dto.PointDTO;
import java.util.List;

@Mapper
public interface PointMapper {
    List<PointDTO> getPointsByUserPk(int userPk);
}
