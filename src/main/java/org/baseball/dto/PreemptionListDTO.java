package org.baseball.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PreemptionListDTO {
    private int quantity;
    private int userPk;
    private int gamePk;
    private String reserveCode;
    private int reservelistPk;
}
