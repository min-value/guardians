package org.baseball.domain.numball;

import org.baseball.domain.point.PointMapper;
import org.baseball.dto.NumballDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class NumballServiceImpl implements NumballService {

    private final NumballMapper mapper;

    @Autowired
    public NumballServiceImpl(NumballMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    private PointMapper pointMapper;

    @Override
    public NumballDTO getOrCreateTodayGame(int userPk) {
        NumballDTO dto = mapper.selectTodayGame(userPk);
        if (dto == null) {
            String answer = generateRandomAnswer();
            dto = new NumballDTO();
            dto.setUserPk(userPk);
            dto.setAnswer(answer);
            mapper.insertNewGame(dto);
        }
        return dto;
    }

    @Override
    public void increaseTryCount(int numballPk) {
        mapper.updateTryCount(numballPk);
    }

    @Override
    public void setSuccess(int numballPk) {
        mapper.markSuccess(numballPk);

        NumballDTO dto = mapper.selectByPk(numballPk);
        dto.setIsSuccess(1);
        checkAndRewardPoint(dto);
    }

    private String generateRandomAnswer() {
        Random rand = new Random();
        int[] nums = rand.ints(0, 10).distinct().limit(3).toArray();
        return "" + nums[0] + nums[1] + nums[2];
    }

    @Override
    @Transactional
    public void updateTries(int numballPk, String triesJson) {
        mapper.updateTries(numballPk, triesJson);
    }

    @Override
    public String getTries(int numballPk) {
        return mapper.selectTries(numballPk);
    }

    public void checkAndRewardPoint(NumballDTO dto) {
        if (dto.getIsSuccess() == 1) {
            int tryCount = dto.getTryCount();
            int reward = 0;

            switch (tryCount) {
                case 1:
                    reward = 50;
                    break;
                case 2:
                    reward = 30;
                    break;
                case 3:
                    reward = 20;
                    break;
                case 4:
                    reward = 10;
                    break;
                case 5:
                    reward = 5;
                    break;
                case 6:
                    reward = 3;
                    break;
                default:
                    reward = 0;
                    break;
            }

            if (reward > 0) {
                Map<String, Object> param = new HashMap<>();
                param.put("userPk", dto.getUserPk());
                param.put("point", reward);

                pointMapper.insertPointReward(param);
                pointMapper.updateTotalPoint(param);
            }
        }
    }
}
