package org.baseball.domain.games;

import org.baseball.dto.GamedetailsDTO;
import org.baseball.dto.RankDTO;
import org.baseball.dto.ScheduleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GamesServiceImpl implements GamesService {

    private final GamesMapper gamesMapper;

    // GamesMapper 주입
    @Autowired
    public GamesServiceImpl(GamesMapper gamesMapper) {
        this.gamesMapper = gamesMapper;
    }

    // DB에서 팀 랭킹 리스트 조회
    @Override
    public List<RankDTO> getTeamRanking() {
        return gamesMapper.getTeamRanking();
    }

    // 특정 월 경기 일정 조회
    @Override
    public List<ScheduleDTO> getScheduleMonth(int year, int month) {
        return gamesMapper.getScheduleMonth(year, month);
    }

    // 상세 경기 정보 조회
    @Override
    public List<GamedetailsDTO> getGameDetails(int year, int month) { return gamesMapper.getGameDetails(year, month); }

}
