package org.baseball.dto;

import lombok.Data;

@Data
public class PreemptionResDTO {
    private int preempted; //0: 이선좌 / 1: 선점 / 2: 좌석 수 부족 / 3: 그 외 오류(로그인, 서버오류 등)
    private int reservelistPk;
    private String errorMsg;
}
