package org.baseball.domain.games;

import org.baseball.dto.TeamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankServiceImpl implements RankService {

    private final RankMapper rankMapper;

    // RankMapper 주입
    @Autowired
    public RankServiceImpl(RankMapper rankMapper) {
        this.rankMapper = rankMapper;
    }

    // DB에서 팀 랭킹 리스트 가져오기
    @Override
    public List<TeamDTO> getTeamRanking() {
        return rankMapper.getTeamRanking();
    }
}
