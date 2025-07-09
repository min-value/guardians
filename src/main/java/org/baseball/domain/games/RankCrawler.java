package org.baseball.domain.games;

import lombok.RequiredArgsConstructor;
import org.baseball.domain.games.GamesMapper;
import org.baseball.dto.RankDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RankCrawler {

    private final GamesMapper gamesMapper;

    @Scheduled(cron = "0 30 00 * * *")
    public void crawlAndSave() {
        try {
            // 랭킹 페이지 URL
            String url = "https://www.koreabaseball.com/Record/TeamRank/TeamRankDaily.aspx";

            // Jsoup으로 HTML 요청
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(10000).get();

            // tbody 태그에서 tr 목록 조회
            Element tbody = doc.select("tbody").first();
            Elements rows = tbody.select("> tr");

            for (Element row : rows) {
                // td 리스트
                Elements cols = row.select("> td");

                // DTO 생성
                RankDTO dto = new RankDTO();

                // 순위
                dto.setRanking(Integer.parseInt(cols.get(0).text()));

                // 팀명, SSG는 신한으로 변경
                String teamName = cols.get(1).text().trim();
                if (teamName.equals("SSG")) {
                    teamName = "신한";
                }
                dto.setTeamName(teamName);

                // 경기수
                dto.setGameCnt(Integer.parseInt(cols.get(2).text()));

                // 승
                dto.setWin(Integer.parseInt(cols.get(3).text()));

                // 패
                dto.setLose(Integer.parseInt(cols.get(4).text()));

                // 무
                dto.setDraw(Integer.parseInt(cols.get(5).text()));

                // 승률
                dto.setWinRate(Double.parseDouble(cols.get(6).text()));

                // 게임차
                dto.setBehind(Double.parseDouble(cols.get(7).text()));

                // 연승/연패
                dto.setWinStreak(cols.get(9).text());

                // DB 저장
                gamesMapper.updateTeamRank(dto);

                // 확인용 로그
                System.out.println(teamName + " 삽입 완료");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
