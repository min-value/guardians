package org.baseball.dto;

import lombok.Data;

@Data
public class PreemptionResDTO {
    private boolean preempted;
    private int reservelistPk;
}
