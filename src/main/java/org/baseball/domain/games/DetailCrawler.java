package org.baseball.domain.games;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import org.baseball.dto.GamedetailsDTO;
import org.baseball.dto.ScheduleDTO;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DetailCrawler {

    private final GamesMapper gamesMapper;

    @Scheduled(cron = "0 30 13 * * *")
    public void crawlDetails() {
        WebDriver driver = initDriver();
        int successCount = 0;

        try {
            List<ScheduleDTO> targets = gamesMapper.getDetails();

            for (ScheduleDTO dto : targets) {
                String url = dto.getDetailUrl();
                if (url == null || url.isBlank()) continue;

                if (gamesMapper.existsGameResult(dto.getGamePk())) continue;

                try {
                    driver.get(url);
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

                    // 날짜, 시간 파싱은 dto에서 가져옴
                    Timestamp ts = Timestamp.valueOf(dto.getGameDate().toLocalDateTime());

                    // 점수
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

                    // 스탯
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

                    // insert
                    GamedetailsDTO detail = new GamedetailsDTO();
                    detail.setGamePk(dto.getGamePk());
                    detail.setDate(ts);
                    detail.setResult(result);
                    detail.setOurScore(myScore);
                    detail.setOppScore(oppScore);
                    detail.setOurHit(hit_our);
                    detail.setOurHomerun(hr_our);
                    detail.setOurStrikeOut(kk_our);
                    detail.setOurBb(bb_our);
                    detail.setOurMiss(err_our);
                    detail.setOppHit(hit_opp);
                    detail.setOppHomerun(hr_opp);
                    detail.setOppStrikeOut(kk_opp);
                    detail.setOppBb(bb_opp);
                    detail.setOppMiss(err_opp);
                    detail.setWinPitcher(winPitcher);
                    detail.setLosePitcher(losePitcher);

                    gamesMapper.insertGameResult(detail);
                    System.out.println("저장 완료: game_pk = " + dto.getGamePk());
                    successCount++;

                } catch (Exception e) {
                    System.out.println("크롤링 실패: game_pk = " + dto.getGamePk());
                    e.printStackTrace();
                }
            }

            System.out.println("크롤링 완료 - 총 저장된 경기 수: " + successCount + "건");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private WebDriver initDriver() {
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
}
