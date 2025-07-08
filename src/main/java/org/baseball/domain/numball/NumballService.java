package org.baseball.domain.numball;

import org.apache.ibatis.annotations.Param;
import org.baseball.dto.NumballDTO;

public interface NumballService {
    NumballDTO getOrCreateTodayGame(int userPk);
    void increaseTryCount(int numballPk);
    void setSuccess(int numballPk);
    void updateTries(@Param("numballPk") int numballPk, @Param("tries") String triesJson);
    String getTries(int numballPk);
}
