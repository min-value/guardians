package org.baseball.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class GamedetailsDTO {
    int gamePk;
    int ourHit;
    int ourHomerun;
    int ourStrikeOut;
    int ourBb;
    int ourMiss;
    int oppHit;
    int oppHomerun;
    int oppStrikeOut;
    int oppBb;
    int oppMiss;
    String winPitcher;
    String losePitcher;
    Timestamp date;
    String result;

    int ourScore;
    int oppScore;
    int oppTeamPk;

    String teamName;

}
