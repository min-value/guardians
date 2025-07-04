package org.baseball.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class RefundDTO {
    private int reservelistPk;
    private int userPk;

    private int gamePk;

    private int isCancel;
    private int isPay;
    private Timestamp cancelDate;
    private int usedPoint;
    private int paidAmount;
    private String impUid;

    private String state;

    // 환불 할 포인트
    private int point;
    private String type;
    private String description;

    private int totalPoint;
}
