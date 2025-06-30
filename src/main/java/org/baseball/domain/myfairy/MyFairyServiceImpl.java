package org.baseball.domain.myfairy;

import org.baseball.dto.MyFairyDTO;
import org.baseball.dto.UserDTO;
import org.baseball.domain.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyFairyServiceImpl implements MyFairyService {

    private final UserMapper userMapper;

    @Autowired
    public MyFairyServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
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

        MyFairyDTO dto = new MyFairyDTO();
        dto.setWinCnt(win);
        dto.setDrawCnt(draw);
        dto.setLoseCnt(lose);
        dto.setTotalCnt(total);
        dto.setWinRate(roundedRate);

        return dto;
    }
}