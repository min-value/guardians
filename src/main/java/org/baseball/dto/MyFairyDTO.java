package org.baseball.dto;

import lombok.Data;

@Data
public class MyFairyDTO {
    private int totalCnt;
    private int winCnt;
    private int loseCnt;
    private int drawCnt;
    private double winRate;
}