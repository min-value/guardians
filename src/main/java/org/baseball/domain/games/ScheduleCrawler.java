package org.baseball.domain.games;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ScheduleCrawler {

    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = init();

        try {
            for (int month = 3; month <= 8; month++) {
                load(driver, month);
                parse(driver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    // 드라이버 설정
    private static WebDriver init() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        return driver;
    }

    // 월별 페이지 로드
    private static void load(WebDriver driver, int month) throws InterruptedException {
        String m = "";
        if (month < 10) {
            m = "0" + month;
        } else {
            m = String.valueOf(month);
        }

        String url = "https://sports.daum.net/schedule/kbo?date=2025" + m;
        driver.get(url);
        Thread.sleep(2000);

        WebElement team = driver.findElement(By.cssSelector("li[data-team-id='384'] > a"));
        team.click();
        Thread.sleep(1000);
    }

    // 경기 리스트 파싱
    private static void parse(WebDriver driver) {
        List<WebElement> games = driver.findElements(By.cssSelector("tbody#scheduleList > tr[data-date]"));

        String lastDate = "";
        String lastArea = "";

        for (WebElement game : games) {
            List<WebElement> tds = game.findElements(By.cssSelector("td"));

            if (tds.size() == 6) {
                String date = lastDate;
                String time = tds.get(0).getText();
                String area = lastArea;
                parseGame(game, date, time, area);
            }

            if (tds.size() == 7) {
                String date = tds.get(0).getText().substring(0, 5).trim();
                String time = tds.get(1).getText();
                String areaRaw = tds.get(2).getText();
                String area = areaRaw.substring(0, 2).trim();

                if ("인천".equals(area)) {
                    area = "연남";
                }

                lastDate = date;
                lastArea = area;

                parseGame(game, date, time, area);
            }
        }
    }

    // 경기 하나 파싱 후 DB 저장
    private static void parseGame(WebElement game, String date, String time, String area) {
        String state = get(game, "td.td_team > span.state_game");
        String team1 = get(game, "td.td_team div.info_team.team_home span.txt_team");
        String team2 = get(game, "td.td_team div.info_team.team_away span.txt_team");
        String score1 = get(game, "td.td_team div.info_team.team_home .num_score");
        String score2 = get(game, "td.td_team div.info_team.team_away .num_score");

        String opp = "";
        Integer oppScore = null;
        Integer ourScore = null;

        if (!"SSG".equals(team1)) {
            opp = team1;
            oppScore = toInt(score1);
            ourScore = toInt(score2);
        } else {
            opp = team2;
            oppScore = toInt(score2);
            ourScore = toInt(score1);
        }

        String result = "";

        if ("경기취소".equals(state)) {
            result = "CANCEL";
            ourScore = 0;
            oppScore = 0;
        } else if (oppScore == null || ourScore == null) {
            result = null;
        } else if (oppScore > ourScore) {
            result = "LOSE";
        } else if (oppScore < ourScore) {
            result = "WIN";
        } else {
            result = "DRAW";
        }

        String dt = "2025-" + date.replace(".", "-") + " " + time;
        Timestamp ts = Timestamp.valueOf(LocalDateTime.parse(dt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        try (
                Connection conn = DriverManager.getConnection("jdbc:mariadb://guardians.cxs4smeqcbch.ap-northeast-2.rds.amazonaws.com:3306/guardians"
                        , "admin"
                        , "rkeldjswm");
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO games (game_date, result, our_score, opp_score, stadium_pk, team_pk) " +
                                "SELECT ?, ?, ?, ?, s.stadium_pk, t.team_pk FROM stadium s, team t " +
                                "WHERE s.location = ? AND t.team_name = ?"
                )
        ) {
            ps.setTimestamp(1, ts);

            if (result == null) {
                ps.setNull(2, Types.VARCHAR);
            } else {
                ps.setString(2, result);
            }

            if (ourScore == null) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, ourScore);
            }

            if (oppScore == null) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, oppScore);
            }

            ps.setString(5, area);
            ps.setString(6, opp);

            int count = ps.executeUpdate();

            if (count > 0) {
                System.out.println("저장 -> 날짜: " + date + ", 상대: " + opp + ", 결과: " + result);

                // 홈게임인 경우에만 homegame에 추가
                if ("연남".equals(area)) {
                    try (PreparedStatement ps2 = conn.prepareStatement(
                            "INSERT IGNORE INTO homegame (game_pk, start_date, end_date) VALUES (LAST_INSERT_ID(), NULL, NULL)"
                    )) {
                        ps2.executeUpdate();
                        System.out.println("-> homegame 테이블에 추가");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.err.println("저장 실패");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 텍스트 추출
    private static String get(WebElement el, String selector) {
        try {
            return el.findElement(By.cssSelector(selector)).getText().trim();
        } catch (Exception e) {
            return "-";
        }
    }

    // 숫자 변환
    private static Integer toInt(String score) {
        if (score != null && score.trim().matches("\\d+")) {
            return Integer.parseInt(score.trim());
        }
        return null;
    }
}
