package org.baseball.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ScheduleDTO {
    int gamePk;
    Timestamp gameDate;
    String result;

    private Integer ourScore;
    private Integer oppScore;

    int stadiumPk;
    int teamPk;

    // StaduumPk
    private String location;
}
