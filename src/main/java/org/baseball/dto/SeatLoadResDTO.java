package org.baseball.dto;

import lombok.Data;

import java.util.Map;

@Data
public class SeatLoadResDTO {
    private boolean error;
    private String errorMsg;
    private Map<String, Object> result;
    private int check;
}
