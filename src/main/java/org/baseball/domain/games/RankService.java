package org.baseball.domain.games;

import org.baseball.dto.TeamDTO;

import java.util.List;

public interface RankService {
    //DB에서 팀 랭킹 조회
    List<TeamDTO> getTeamRanking();
}
