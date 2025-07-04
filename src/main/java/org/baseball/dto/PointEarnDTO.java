package org.baseball.dto;

import lombok.Data;

@Data
public class PointEarnDTO {
    private int reservelistPk;
    private int userPk;
    private int gamePk;
    private int paidAmount;
    private int predict;
    private String result;
    private int totalPoint;
}