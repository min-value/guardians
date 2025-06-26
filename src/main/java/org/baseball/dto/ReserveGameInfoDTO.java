package org.baseball.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ReserveGameInfoDTO {
    private int gamePk;
    private int oppTeamPk;
    private String oppTeamName;
    private Timestamp gameDate;
}
