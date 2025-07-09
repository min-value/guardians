package org.baseball.domain.games;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import org.baseball.dto.GamedetailsDTO;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DetailCrawler {

    private final GamesMapper gamesMapper;

    @Scheduled(cron = "0 40 00 * * *")
    public void crawlDetails() {
        WebDriver driver = initDriver();
        Set<Integer> processedPk = new HashSet<>();

        try {
            for (int month = 3; month <= 8; month++) {
                moveToPage(driver, month);
                parseGames(driver, processedPk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private WebDriver initDriver() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        return driver;
    }

    private void moveToPage(WebDriver driver, int month) {
        String m = (month < 10 ? "0" : "") + month;
        String url = "https://sports.daum.net/schedule/kbo?date=2025" + m;
        driver.get(url);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li[data-team-id='384'] > a"))).click();
    }

    private void parseGames(WebDriver driver, Set<Integer> processedPk) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tbody#scheduleList")));
        List<WebElement> rows = driver.findElements(By.cssSelector("tbody#scheduleList > tr[data-date]"));

        for (WebElement row : rows) {
            String state = "";
            try {
                state = row.findElement(By.cssSelector("span.state_game")).getText().trim();
            } catch (Exception ignored) {}

            if (state.equals("경기전") || state.equals("경기취소")) {
                System.out.println("미진행, 취소 경기");
                continue;
            }

            WebElement linkTag;
            try {
                linkTag = row.findElement(By.cssSelector("td.td_btn > a"));
            } catch (Exception e) {
                continue;
            }

            String link = linkTag.getAttribute("href");
            if (link == null || link.isEmpty()) continue;

            ((JavascriptExecutor) driver).executeScript("window.open('" + link + "', '_blank');");
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(d -> driver.getWindowHandles().size() > 1);

            List<String> tabs = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(tabs.size() - 1));

            try {
                // 경기 일시 파싱
                WebElement datetimeTag = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("dl.list_gameinfo > dd")));
                String[] tokens = datetimeTag.getText().trim().split(" ");
                if (tokens.length < 2) {
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

                // 점수 파싱
                WebElement box = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.box_comp.box_record")));
                List<WebElement> nameTags = box.findElements(By.cssSelector("a.txt_teamcard"));
                List<WebElement> scoreTags = box.findElements(By.cssSelector("span.num_team"));

                String name1 = nameTags.get(0).getText().trim();
                String name2 = nameTags.get(1).getText().trim();
                String point1 = scoreTags.get(0).getText().trim();
                String point2 = scoreTags.get(1).getText().trim();

                boolean isVs1Our = name1.equals("SSG");
                int myScore = Integer.parseInt(isVs1Our ? point1 : point2);
                int oppScore = Integer.parseInt(isVs1Our ? point2 : point1);
                String result = myScore > oppScore ? "WIN" : (myScore < oppScore ? "LOSE" : "DRAW");

                // 세부 스탯 파싱
                int hit_our = 0, hr_our = 0, kk_our = 0, err_our = 0, bb_our = 0;
                int hit_opp = 0, hr_opp = 0, kk_opp = 0, err_opp = 0, bb_opp = 0;

                List<WebElement> statGroups = driver.findElements(By.cssSelector("ul.list_graph > li"));
                for (WebElement group : statGroups) {
                    String title = group.findElement(By.cssSelector("span.tit_graph")).getText().trim();
                    if (!title.matches("안타|홈런|탈삼진|실책|사사구")) continue;

                    List<WebElement> nums = group.findElements(By.cssSelector("span.num_g"));
                    if (nums.size() < 2) continue;

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

                // 승/패 투수
                String winPitcher = "", losePitcher = "";
                for (WebElement li : driver.findElements(By.cssSelector("ul.list_pitcher > li"))) {
                    String infoClass = li.findElement(By.cssSelector("span.player_info")).getAttribute("class");
                    String name = li.findElement(By.cssSelector("strong.txt_name > a")).getText().trim();
                    if (infoClass.contains("type_win")) winPitcher = name;
                    else if (infoClass.contains("type_lose")) losePitcher = name;
                }

                Timestamp ts = Timestamp.valueOf(gameDate);
                Integer gamePk = gamesMapper.getGamePkByDate(ts);

                if (gamePk == null) {
                    System.out.println("game_pk 없음 -> " + gameDate);
                } else if (processedPk.contains(gamePk)) {
                    continue;
                } else if (gamesMapper.existsGameResult(gamePk)) {
                    processedPk.add(gamePk);
                    System.out.println("이미 존재하는 기록 -> game_pk = " + gamePk);
                } else {
                    // 정상 저장되면 로그 출력
                    GamedetailsDTO dto = new GamedetailsDTO();
                    dto.setGamePk(gamePk);
                    dto.setDate(ts);
                    dto.setResult(result);
                    dto.setOurScore(myScore);
                    dto.setOppScore(oppScore);
                    dto.setOurHit(hit_our);
                    dto.setOurHomerun(hr_our);
                    dto.setOurStrikeOut(kk_our);
                    dto.setOurBb(bb_our);
                    dto.setOurMiss(err_our);
                    dto.setOppHit(hit_opp);
                    dto.setOppHomerun(hr_opp);
                    dto.setOppStrikeOut(kk_opp);
                    dto.setOppBb(bb_opp);
                    dto.setOppMiss(err_opp);
                    dto.setWinPitcher(winPitcher);
                    dto.setLosePitcher(losePitcher);

                    gamesMapper.insertGameResult(dto);
                    processedPk.add(gamePk);
                    System.out.println("DB 저장 완료 -> game_pk = " + gamePk);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            driver.close();
            driver.switchTo().window(tabs.get(0));
        }
    }
}
