package org.baseball.domain.games;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.sql.*;
import java.time.*;
import java.util.*;

public class DetailCrawiler {

    private static Connection conn;

    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mariadb://guardians.cxs4smeqcbch.ap-northeast-2.rds.amazonaws.com:3306/guardians"
                    , "admin"
                    , "rkeldjswm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WebDriver driver = initDriver();

        try {
            parseAllMonths(driver);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
            try {
                conn.close();
            } catch (Exception ignored) {}
        }
    }

    // 드라이버 초기 설정
    private static WebDriver initDriver() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        return driver;
    }

    // 월별 전체 파싱
    private static void parseAllMonths(WebDriver driver) {
        for (int month = 3; month <= 8; month++) {
            moveToPage(driver, month);
            parseGames(driver);
        }
    }

    // 페이지 이동 및 팀 탭 클릭
    private static void moveToPage(WebDriver driver, int month) {
        String m = (month < 10 ? "0" : "") + month;
        String url = "https://sports.daum.net/schedule/kbo?date=2025" + m;
        driver.get(url);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li[data-team-id='384'] > a"))).click();
    }

    // 경기 목록 파싱
    private static void parseGames(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        List<WebElement> rows = driver.findElements(By.cssSelector("tbody#scheduleList > tr[data-date]"));

        for (WebElement row : rows) {
            String dateStr = row.getAttribute("data-date").substring(4);
            String state = "";

            try {
                state = row.findElement(By.cssSelector("span.state_game")).getText().trim();
            } catch (Exception ignored) {}

            if (state.equals("경기전") || state.equals("경기취소")) {
                System.out.println(dateStr + " : " + state);
                continue;
            }

            WebElement linkTag;
            try {
                linkTag = row.findElement(By.cssSelector("td.td_btn > a"));
            } catch (Exception e) {
                continue;
            }

            String link = linkTag.getAttribute("href");
            if (link == null || link.isEmpty()) {
                continue;
            }

            ((JavascriptExecutor) driver).executeScript("window.open('" + link + "', '_blank');");

            WebDriverWait tabWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            tabWait.until(d -> driver.getWindowHandles().size() > 1);

            Set<String> handles = driver.getWindowHandles();
            List<String> tabs = new ArrayList<>(handles);
            driver.switchTo().window(tabs.get(tabs.size() - 1));

            System.out.println(dateStr + " 상세 페이지 이동 완료");

            try {
                WebElement datetimeTag = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("dl.list_gameinfo > dd")));
                String[] tokens = datetimeTag.getText().trim().split(" ");
                if (tokens.length < 2) {
                    System.out.println("날짜/시간 형식 이상함 : " + datetimeTag.getText());
                    driver.close();
                    driver.switchTo().window(tabs.get(0));
                    continue;
                }

                String dateToken = tokens[0].split("\\(")[0].replace(".", "");
                String timeToken = tokens[1];
                int month = Integer.parseInt(dateToken.substring(0, 2));
                int day = Integer.parseInt(dateToken.substring(2, 4));
                String[] hm = timeToken.split(":");
                int hour = Integer.parseInt(hm[0]);
                int minute = Integer.parseInt(hm[1]);
                LocalDateTime gameDate = LocalDateTime.of(2025, month, day, hour, minute);

                WebElement box = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.box_comp.box_record")));
                List<WebElement> nameTags = box.findElements(By.cssSelector("a.txt_teamcard"));
                List<WebElement> scoreTags = box.findElements(By.cssSelector("span.num_team"));

                String name1 = nameTags.get(0).getText().trim();
                String name2 = nameTags.get(1).getText().trim();
                String point1 = scoreTags.get(0).getText().trim();
                String point2 = scoreTags.get(1).getText().trim();

                String ourTeam = "SSG";
                boolean isVs1Our = name1.equals(ourTeam);
                String runOur = isVs1Our ? point1 : point2;
                String runOpp = isVs1Our ? point2 : point1;

                int myScore = Integer.parseInt(runOur);
                int oppScore = Integer.parseInt(runOpp);
                String result = myScore > oppScore ? "WIN" : (myScore < oppScore ? "LOSE" : "DRAW");

                List<WebElement> statGroups = driver.findElements(By.cssSelector("ul.list_graph > li"));
                int hit_our = 0;
                int hr_our = 0;
                int kk_our = 0;
                int err_our = 0;
                int bb_our = 0;

                int hit_opp = 0;
                int hr_opp = 0;
                int kk_opp = 0;
                int err_opp = 0;
                int bb_opp = 0;

                for (WebElement group : statGroups) {
                    String title = group.findElement(By.cssSelector("span.tit_graph")).getText().trim();
                    if (!title.matches("안타|홈런|탈삼진|실책|사사구")) {
                        continue;
                    }

                    List<WebElement> nums = group.findElements(By.cssSelector("span.num_g"));
                    if (nums.size() < 2) {
                        continue;
                    }

                    int val1 = Integer.parseInt(nums.get(0).getText().trim());
                    int val2 = Integer.parseInt(nums.get(1).getText().trim());

                    int a = isVs1Our ? val1 : val2;
                    int b = isVs1Our ? val2 : val1;

                    switch (title) {
                        case "안타": hit_our = a; hit_opp = b; break;
                        case "홈런": hr_our = a; hr_opp = b; break;
                        case "탈삼진": kk_our = a; kk_opp = b; break;
                        case "실책": err_our = a; err_opp = b; break;
                        case "사사구": bb_our = a; bb_opp = b; break;
                    }
                }

                String winPitcher = "", losePitcher = "";
                List<WebElement> pitcherList = driver.findElements(By.cssSelector("ul.list_pitcher > li"));
                for (WebElement li : pitcherList) {
                    String infoClass = li.findElement(By.cssSelector("span.player_info")).getAttribute("class");
                    String name = li.findElement(By.cssSelector("strong.txt_name > a")).getText().trim();

                    if (infoClass.contains("type_win")) winPitcher = name;
                    else if (infoClass.contains("type_lose")) losePitcher = name;
                }

                insertGameResult(gameDate, result, hit_our, hr_our, kk_our, bb_our, err_our,
                        hit_opp, hr_opp, kk_opp, bb_opp, err_opp, winPitcher, losePitcher);

            } catch (Exception e) {
                e.printStackTrace();
            }

            driver.close();
            driver.switchTo().window(tabs.get(0));
        }
    }

    // DB 저장
    private static void insertGameResult(LocalDateTime gameDate, String result, int hit_our, int hr_our,
                                         int kk_our, int bb_our, int err_our,
                                         int hit_opp, int hr_opp, int kk_opp, int bb_opp, int err_opp,
                                         String winPitcher, String losePitcher) throws SQLException {

        String selectSql = "SELECT game_pk FROM games WHERE game_date = ?";
        PreparedStatement selectStmt = conn.prepareStatement(selectSql);
        selectStmt.setTimestamp(1, Timestamp.valueOf(gameDate));
        ResultSet rs = selectStmt.executeQuery();

        if (!rs.next()) {
            System.out.println("game_pk 없음 -> " + gameDate);
            return;
        }

        int gamePk = rs.getInt("game_pk");
        rs.close();
        selectStmt.close();

        String checkSql = "SELECT COUNT(*) FROM game_result WHERE game_pk = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
        checkStmt.setInt(1, gamePk);
        ResultSet checkRs = checkStmt.executeQuery();
        if (checkRs.next() && checkRs.getInt(1) > 0) {
            System.out.println("이미 존재하는 pk : game_pk = " + gamePk);
            checkRs.close();
            checkStmt.close();
            return;
        }
        checkRs.close();
        checkStmt.close();

        String insertSql = "INSERT INTO game_result " +
                "(game_pk, our_hit, our_homerun, our_strikeout, our_bb, our_miss, " +
                "opp_hit, opp_homerun, opp_strikeout, opp_bb, opp_miss, " +
                "result, win_pitcher, lose_pitcher, date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement insertStmt = conn.prepareStatement(insertSql);
        insertStmt.setInt(1, gamePk);
        insertStmt.setInt(2, hit_our);
        insertStmt.setInt(3, hr_our);
        insertStmt.setInt(4, kk_our);
        insertStmt.setInt(5, bb_our);
        insertStmt.setInt(6, err_our);
        insertStmt.setInt(7, hit_opp);
        insertStmt.setInt(8, hr_opp);
        insertStmt.setInt(9, kk_opp);
        insertStmt.setInt(10, bb_opp);
        insertStmt.setInt(11, err_opp);
        insertStmt.setString(12, result);
        insertStmt.setString(13, winPitcher);
        insertStmt.setString(14, losePitcher);
        insertStmt.setTimestamp(15, Timestamp.valueOf(gameDate));
        insertStmt.executeUpdate();
        insertStmt.close();

        System.out.println("DB 저장 완료 : " + gameDate + " game_pk : " + gamePk);
    }
}
