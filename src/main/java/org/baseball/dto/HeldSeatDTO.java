package org.baseball.dto;

import lombok.Data;

@Data
public class HeldSeatDTO {
    private int reservelistPk;
    private int zonePk;
    private String seatNum;
}
