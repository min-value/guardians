package org.baseball.dto;

import lombok.Data;

@Data
public class MyFairyDTO {
    private int totalCnt;
    private int winCnt;
    private int loseCnt;
    private int drawCnt;
    private double winRate;
    private String winRateFormatted;

    private int ourHit;
    private int ourHomerun;
    private int ourStrikeout;
    private int ourBB;
    private int ourMiss;

    private int oppHit;
    private int oppHomerun;
    private int oppStrikeout;
    private int oppBB;
    private int oppMiss;

}