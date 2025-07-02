package org.baseball.dto;

import lombok.Data;

import java.util.List;

@Data
public class PreemptDeleteReqDTO {
    private int gamePk;
    private List<String> seats;
    private int zonePk;
    private int reservelistPk;
}
