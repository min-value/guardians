package org.baseball.dto;

import lombok.Data;
import java.sql.Date;
import java.sql.Timestamp;

@Data
public class NumballDTO {
    private int numballPk;
    private int userPk;
    private Date gameDate;
    private String answer;
    private int tryCount;
    private int isSuccess;
    private String tries;
    private Timestamp createdAt;
}
