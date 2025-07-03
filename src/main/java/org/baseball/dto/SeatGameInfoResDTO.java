package org.baseball.dto;

import lombok.Data;

import java.util.Map;

@Data
public class SeatGameInfoResDTO {
    private boolean error;
    private String errorMsg;
    private Map<String, Object> result;
}
