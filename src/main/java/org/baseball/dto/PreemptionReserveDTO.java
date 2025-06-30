package org.baseball.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PreemptionReserveDTO {
    private int reservelistPk;
    private int zonePk;
    private String seatNum;
    private int reservePk;
}
