package org.baseball.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ScheduleDTO {
    int gamePk;
    Timestamp gameDate;
    String result;
    int ourScore;
    int oppScore;
    int stadiumPk;
    int teamPk;
}
