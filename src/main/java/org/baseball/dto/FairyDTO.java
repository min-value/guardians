package org.baseball.dto;

import lombok.Data;

@Data
public class FairyDTO {
    private int place;
    private int userNo;
    private String userName;
    private int totalCnt;
    private int winCnt;
    private int loseCnt;
    private int drawCnt;
    private String ratio;

}
