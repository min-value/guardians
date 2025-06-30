package org.baseball.domain.myfairy;

import org.baseball.dto.MyFairyDTO;
import org.baseball.dto.UserDTO;
import org.baseball.domain.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MyFairyServiceImpl implements MyFairyService {

    private final UserMapper userMapper;
    private final MyFairyMapper myFairyMapper;

    @Autowired
    public MyFairyServiceImpl(UserMapper userMapper, MyFairyMapper myFairyMapper) {
        this.userMapper = userMapper;
        this.myFairyMapper = myFairyMapper;
    }

    @Override
    public MyFairyDTO getMyFairyInfo(int userPk) {
        UserDTO user = userMapper.findUserByPk(userPk);

        int win = user.getWinCnt();
        int draw = user.getDrawCnt();
        int lose = user.getLoseCnt();
        int total = win + draw + lose;

        int wlTotal = win + lose;
        double rate = (double) (win + 1) / (wlTotal + 2);
        double roundedRate = Math.round(rate * 1000.0) / 1000.0;

        Map<String, Object> resultMap = myFairyMapper.selectFairyGameResults(userPk);
        if (resultMap == null) {
            resultMap = new HashMap<>();
        }

        MyFairyDTO dto = new MyFairyDTO();
        dto.setWinCnt(win);
        dto.setDrawCnt(draw);
        dto.setLoseCnt(lose);
        dto.setTotalCnt(total);
        dto.setWinRate(roundedRate);

        dto.setOurHit(castToInt(resultMap.get("ourHit")));
        dto.setOurHomerun(castToInt(resultMap.get("ourHomerun")));
        dto.setOurStrikeout(castToInt(resultMap.get("ourStrikeout")));
        dto.setOurBB(castToInt(resultMap.get("ourBB")));
        dto.setOurMiss(castToInt(resultMap.get("ourMiss")));

        dto.setOppHit(castToInt(resultMap.get("oppHit")));
        dto.setOppHomerun(castToInt(resultMap.get("oppHomerun")));
        dto.setOppStrikeout(castToInt(resultMap.get("oppStrikeout")));
        dto.setOppBB(castToInt(resultMap.get("oppBB")));
        dto.setOppMiss(castToInt(resultMap.get("oppMiss")));

        return dto;
    }

    private int castToInt(Object value) {
        if (value == null) return 0;
        if (value instanceof Number) return ((Number) value).intValue();
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}