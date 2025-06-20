package org.baseball.domain.admin;

import org.apache.ibatis.annotations.Mapper;
import org.baseball.dto.GamesInfoDTO;
import org.baseball.dto.ReserveInfoDTO;

import java.util.List;

@Mapper
public interface AdminMapper {
    List<ReserveInfoDTO> showReserveList();
    List<GamesInfoDTO> showGamesList();
    List<GamesInfoDTO> showGamesAddList();
}
