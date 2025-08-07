package org.baseball.dto;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class NewsDTO {
    private String n_title;
    private String n_writer;
    private String n_content;
    private String img_url;
    private String news_url;
    private Timestamp n_writeDate;
}
