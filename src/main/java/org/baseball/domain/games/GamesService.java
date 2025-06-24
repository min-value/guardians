package org.baseball.domain.games;

import org.baseball.dto.RankDTO;
import org.baseball.dto.ScheduleDTO;

import java.util.List;

public interface GamesService {
    //DB에서 팀 랭킹 조회
    List<RankDTO> getTeamRanking();

    // 경기 일정 조회
    List<ScheduleDTO> getGameSchedule();
}
