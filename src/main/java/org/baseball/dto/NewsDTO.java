package org.baseball.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class NewsDTO {
    private String n_title;
    private String n_writer;
    private String n_content;
    private int n_year;
    private int n_month;
    private int n_date;
    private int n_hour;
    private int n_minute;
    private String img_url;
    private String news_url;
}
