package org.baseball.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PostDto {
    private int post_pk;
    private String title;
    private String p_content;
    private Timestamp p_date;
    private String user_name;

}
