package org.baseball.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CommentDTO {
    private int comment_pk;
    private String c_content;
    private Timestamp c_date;
    private String user_name;
    private int user_pk;
    private int post_pk;
}
