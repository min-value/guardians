package org.baseball.dto;

import lombok.Data;

@Data
public class TeamDTO {
    private int teamPk;
    private int ranking;
    private String teamName;
    private int win;
    private int lose;
    private int draw;
    private double winRate;
    private double behind;
    private int gameCnt;
    private String winStreak;
}
