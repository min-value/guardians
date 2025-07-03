package org.baseball.dto;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseReqDTO {
    private int reservelist_pk;
    private String impUid;
    private int used_point;
    private int total_amount;
    private int paid_amount;
    private String apply_num;
    private String card_code;
    private String card_name;
    private String card_number;
    private int user_pk;
    private int game_pk;
    private int zone_pk;
    private List<String> seats;
    private List<Integer> discount_pk;
    private int dPk;
}
