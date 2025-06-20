package org.baseball.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class HomeGameDTO {
    private int gameNo;
    private Timestamp startTime;
    private Timestamp endTime;
}
