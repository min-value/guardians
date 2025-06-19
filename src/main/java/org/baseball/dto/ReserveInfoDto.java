package org.baseball.dto;

import lombok.Data;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
public class ReserveInfoDto {
    private int no;
    private String name;
    private int reserveNo;
    private int seatCount;
    private String zone;
    private String seat;
    private boolean isCancel;
    private boolean isPay;
    private String status;
    private Timestamp gameDate;
    private Timestamp cancelDate;

    private String gameDateFormat;
    private String cancelDateFormat;

    private String[] zones;
    private String[] seats;
    private String[] zoneAndSeat;
}
