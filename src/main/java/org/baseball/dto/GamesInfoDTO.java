package org.baseball.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class GamesInfoDTO {
    private int no;
    private int gameNo;
    private String ourTeam;
    private String opponentTeam;
    private String ourScore;
    private String opponentScore;
    private String result;
    private String gameDate;

    private int status;
}
