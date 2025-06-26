package org.baseball.domain.games;

import org.baseball.dto.RankDTO;
import org.baseball.dto.ScheduleDTO;

import java.util.List;

public interface GamesService {
    //DB에서 팀 랭킹 조회
    List<RankDTO> getTeamRanking();

    // 특정 년도, 월 경기 조회
    List<ScheduleDTO> getScheduleMonth(int year, int month);
}
