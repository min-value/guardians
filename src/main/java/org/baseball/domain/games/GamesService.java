package org.baseball.domain.games;

import org.baseball.dto.GamedetailsDTO;
import org.baseball.dto.RankDTO;
import org.baseball.dto.ScheduleDTO;

import java.util.List;

public interface GamesService {
    //DB에서 팀 랭킹 조회
    List<RankDTO> getTeamRanking();

    // 특정 년도, 월 경기 조회
    List<ScheduleDTO> getScheduleMonth(int year, int month);

    // 상세 경기 정보 조회
    List<GamedetailsDTO> getGameDetails(int year, int month);

    // 페이지네이션용 상세 조회
    List<GamedetailsDTO> getDetailList(int year, int month, int page, int size);

    // 전체 개수 조회
    int getDetailCount(int year, int month);
}
