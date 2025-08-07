package org.baseball.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class PostDTO {
    private int post_pk;
    private String title;
    private String p_content;
    private Timestamp p_date;
    private String user_name;
    private int user_pk;

}
