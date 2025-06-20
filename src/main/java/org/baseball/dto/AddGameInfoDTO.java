package org.baseball.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AddGameInfoDTO {
    private int gameNo;
    private String opponentTeamName;
    private String gameDate;
    private String stadium;
    private Timestamp startTime;
    private Timestamp endTime;
}
