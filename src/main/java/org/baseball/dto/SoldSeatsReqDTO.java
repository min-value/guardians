package org.baseball.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SoldSeatsReqDTO {
    private int gamePk;
    private int zonePk;
}
