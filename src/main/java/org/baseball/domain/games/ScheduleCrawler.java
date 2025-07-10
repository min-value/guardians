package org.baseball.domain.games;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import org.baseball.dto.ScheduleDTO;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleCrawler {

    private final GamesMapper gamesMapper;

    @Scheduled(cron = "0 25 13 * * *")
    public void crawlSchedule() {
        WebDriver driver = init();

        try {
            for (int month = 3; month <= 12; month++) {
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
    private WebDriver init() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        // 최신 Headless 모드 (GUI와 동일 렌더링)
        options.addArguments("--headless=new");

        // 안정성 옵션
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        // 사람처럼 보이게 하는 옵션들
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36");

        WebDriver driver = new ChromeDriver(options);
        return driver;
    }

    // 월별 페이지 로드
    private static void load(WebDriver driver, int month) throws InterruptedException {
        String m = (month < 10) ? "0" + month : String.valueOf(month);
        String url = "https://sports.daum.net/schedule/kbo?date=2025" + m;
        driver.get(url);
        Thread.sleep(2000);

        WebElement team = driver.findElement(By.cssSelector("li[data-team-id='384'] > a"));
        team.click();
        Thread.sleep(1000);
    }

    // 경기 리스트 파싱
    private void parse(WebDriver driver) {
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

                if ("인천".equals(area)) area = "연남";

                lastDate = date;
                lastArea = area;

                parseGame(game, date, time, area);
            }
        }
    }

    // 경기 하나 파싱 후 DB 저장
    private void parseGame(WebElement game, String date, String time, String area) {
        String state = get(game, "td.td_team > span.state_game");
        String team1 = get(game, "td.td_team div.info_team.team_home span.txt_team");
        String team2 = get(game, "td.td_team div.info_team.team_away span.txt_team");
        String score1 = get(game, "td.td_team div.info_team.team_home .num_score");
        String score2 = get(game, "td.td_team div.info_team.team_away .num_score");

        String opp = !"SSG".equals(team1) ? team1 : team2;
        Integer oppScore = toInt(!"SSG".equals(team1) ? score1 : score2);
        Integer ourScore = toInt(!"SSG".equals(team1) ? score2 : score1);

        String result;
        if ("경기취소".equals(state)) {
            result = "CANCEL";
            ourScore = 0;
            oppScore = 0;
        } else if (ourScore == null || oppScore == null) {
            result = null;
        } else if (ourScore > oppScore) {
            result = "WIN";
        } else if (ourScore < oppScore) {
            result = "LOSE";
        } else {
            result = "DRAW";
        }

        // 날짜 + 시간 파싱
        String dt = "2025-" + date.replace(".", "-") + " " + time;
        Timestamp gameDate = null;
        try {
            gameDate = Timestamp.valueOf(LocalDateTime.parse(dt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        } catch (Exception e) {
            return; // 날짜 파싱 실패 시 해당 경기 스킵
        }

        String detailUrl = null;
        try {
            WebElement linkTag = game.findElement(By.cssSelector("td.td_btn > a"));

            String rawText = linkTag.getDomProperty("textContent").trim();
            WebElement span = linkTag.findElement(By.cssSelector("span.inner_txt"));
            String innerText = span.getText().trim();
            String linkText = rawText.replace(innerText, "").trim();

            if (!"문자중계".equals(linkText)) {
                detailUrl = linkTag.getAttribute("href");
                if (!detailUrl.startsWith("http")) {
                    detailUrl = "https://sports.daum.net" + detailUrl;
                }
            }
        } catch (Exception ignored) {}



        int stadiumPk = gamesMapper.getStadiumPk(area);
        int teamPk = gamesMapper.getTeamPk(opp);

        ScheduleDTO dto = new ScheduleDTO();
        dto.setGameDate(gameDate);
        dto.setResult(result);
        dto.setOurScore(ourScore);
        dto.setOppScore(oppScore);
        dto.setStadiumPk(stadiumPk);
        dto.setTeamPk(teamPk);
        dto.setLocation(area);
        dto.setDetailUrl(detailUrl);

        ScheduleDTO existing = gamesMapper.findSchedule(gameDate.toString(), area, opp);

        if (existing != null) {
            dto.setGamePk(existing.getGamePk());

            if (existing.getDetailUrl() == null && detailUrl != null) {
                gamesMapper.updateDetailUrl(dto);
                System.out.println("URL만 추가됨 날짜: " + date + ", 상대: " + opp);
            }

            if (existing.getResult() == null && result != null) {
                gamesMapper.updateSchedule(dto);
                System.out.println("경기정보 업데이트 날짜: " + date + ", 상대: " + opp + ", 결과: " + result);
            } else {
                System.out.println("경기정보 존재 날짜: " + date + ", 상대: " + opp);
            }
        } else {
            gamesMapper.insertSchedule(dto);
            System.out.println("신규 편성 경기 저장 날짜: " + date + ", 상대: " + opp + ", 결과: " + result);
        }
    }

    // 셀렉터 텍스트 추출
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
