package org.baseball.domain.numball;

import org.baseball.dto.NumballDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class NumballServiceImpl implements NumballService {

    private final NumballMapper mapper;

    @Autowired
    public NumballServiceImpl(NumballMapper mapper) {
        this.mapper = mapper;
    }

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
}
