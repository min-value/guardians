package org.baseball.domain.games;

import org.apache.ibatis.annotations.Mapper;
import org.baseball.dto.TeamDTO;

import java.util.List;

@Mapper
public interface RankMapper {
    // DB에서 팀 랭킹 조회
    List<TeamDTO> getTeamRanking();
}