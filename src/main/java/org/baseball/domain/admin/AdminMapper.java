package org.baseball.domain.admin;

import org.apache.ibatis.annotations.Mapper;
import org.baseball.dto.ReserveInfoDto;

import java.util.List;

@Mapper
public interface AdminMapper {
    List<ReserveInfoDto> showReserveList();
}
