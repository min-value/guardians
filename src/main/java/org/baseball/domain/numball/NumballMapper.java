package org.baseball.domain.numball;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.baseball.dto.NumballDTO;

@Mapper
public interface NumballMapper {

    // 오늘 날짜의 게임 조회
    NumballDTO selectTodayGame(int userPk);
    // 새로운 게임 insert
    void insertNewGame(NumballDTO dto);
    // 시도 횟수 update
    void updateTryCount(int numballPk);
    // 성공 여부 설정
    void markSuccess(int numballPk);
    // 시도 내역 저장
    int updateTries(@Param("numballPk") int numballPk, @Param("tries") String triesJson);
    // 저장된 시도 내역 조회
    String selectTries(@Param("numballPk") int numballPk);
}
