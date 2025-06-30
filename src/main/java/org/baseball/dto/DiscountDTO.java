package org.baseball.dto;

import lombok.Data;

@Data
public class DiscountDTO {
    private int discountPk;
    private String type;
    private double disRate;
}
