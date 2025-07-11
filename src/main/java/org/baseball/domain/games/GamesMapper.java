package org.baseball.domain.games;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.baseball.dto.GamedetailsDTO;
import org.baseball.dto.RankDTO;
import org.baseball.dto.ScheduleDTO;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface GamesMapper {
    // 팀 랭킹 조회
    List<RankDTO> getTeamRanking();

    // 경기 스케줄 조회
    List<ScheduleDTO> getScheduleMonth(@Param("year") int year, @Param("month") int month);

    // 경기장 비교용
    int getStadiumPk(@Param("location") String location);
    
    // 팀명 비교용
    int getTeamPk(@Param("teamName") String teamName);

    // 경기 존재 여부 확인
    ScheduleDTO findSchedule(@Param("gameDate") String gameDate, @Param("location") String location, @Param("teamName") String teamName);

    // 신규 경기 추가
    void insertSchedule(ScheduleDTO dto);

    // 경기 결과 업데이트
    void updateSchedule(ScheduleDTO dto);

    // 랭킹 업데이트
    void updateTeamRank(RankDTO dto);
    
    // 상세정보 조회
    List<GamedetailsDTO> getGameDetails(@Param("year") int year, @Param("month") int month);

    // game_pk 가져오기
    Integer getGamePkByDate(@Param("date") Timestamp date);

    // game_result 존재 여부
    boolean existsGameResult(@Param("gamePk") int gamePk);

    // game_result 삽입
    void insertGameResult(GamedetailsDTO dto);


    // 페이지네이션용 목록
    List<GamedetailsDTO> selectDetailList(
            @Param("year") int year,
            @Param("month") int month,
            @Param("offset") int offset,
            @Param("size") int size
    );

    // 전체 개수 조회
    int countDetail(@Param("year") int year, @Param("month") int month);
}
