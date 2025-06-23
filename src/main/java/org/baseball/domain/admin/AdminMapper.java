package org.baseball.domain.admin;

import org.apache.ibatis.annotations.Mapper;
import org.baseball.dto.AddGameInfoDTO;
import org.baseball.dto.GamesInfoDTO;
import org.baseball.dto.HomeGameDTO;
import org.baseball.dto.ReserveInfoDTO;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMapper {
    int countReservation();
    int countGames(Map<String, Object> param);
    int countHomeGames();
    List<ReserveInfoDTO> showReservationList(Map<String, Object> param);
    List<GamesInfoDTO> showGamesList(Map<String, Object> param);
    List<GamesInfoDTO> showGamesAddList(Map<String, Object> param);
    AddGameInfoDTO showAddGameInfo(int gameNo);
    int addHomeGame(HomeGameDTO homeGameDTO);
}
