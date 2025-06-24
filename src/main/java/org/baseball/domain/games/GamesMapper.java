package org.baseball.domain.games;

import org.apache.ibatis.annotations.Mapper;
import org.baseball.dto.RankDTO;
import org.baseball.dto.ScheduleDTO;

import java.util.List;

@Mapper
public interface GamesMapper {
    // 팀 랭킹 조회
    List<RankDTO> getTeamRanking();

    // 경기 스케줄 조회
    List<ScheduleDTO> getGameSchedule();
}