package org.baseball.dto;

import java.sql.Timestamp;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class PointDTO {
    private int pointPk;
    private int userPk;
    private int point;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp date;
    private String day;

    private String type;
    private String description;

    public String getFormattedDate() {
        if (date == null || day == null) return "";
        return String.format("%tF (%s)", date, day);
    }
}
