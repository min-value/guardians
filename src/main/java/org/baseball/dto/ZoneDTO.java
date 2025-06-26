package org.baseball.dto;

import lombok.Data;

@Data
public class ZoneDTO {
    private int zonePk;
    private String zoneName;
    private String zoneColor;
    private int cost;
    private int totalNum;
}
