package org.baseball.dto;

import lombok.Data;

import java.util.List;

@Data
public class    PreemptionDTO {
    private int quantity;
    private int gamePk;
    private int zonePk;
    private List<String> seats;
}
