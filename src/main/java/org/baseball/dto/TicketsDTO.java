package org.baseball.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TicketsDTO {
    private int gameNo;
    private Timestamp gameDate;

    private String ourTeam;
    private String opponentTeam;
    private String stadium;
    private Timestamp startDate;
    private String startDateLocal;
    private Timestamp endDate;

    private String date;
    private String day;
    private String time;

    private int dayIndex;
    private String[] days = {"월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"};
}
