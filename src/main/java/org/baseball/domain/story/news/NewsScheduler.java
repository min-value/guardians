package org.baseball.domain.story.news;

import org.baseball.dto.NewsDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NewsScheduler {

    @Autowired
    private NewsService newsService;

    @Scheduled(fixedRate = 259200000)  // 3일마다 실행 
    public void crawlNews(){
//        try{
//            Document doc = Jsoup.connect("https://sports.daum.net/baseball").get();
//            Elements newsHrefs = doc.select(".list_rank > li");
//            int count = 0;
//            for(Element item : newsHrefs){
//                if (count >= 5) break;
//                Element linkTag = item.selectFirst("a");
//                if(linkTag !=null){
//                    String href = linkTag.absUrl("href");
//                    Document doc2 = Jsoup.connect(href).get();
//
//                    Element newsName = doc2.selectFirst(".tit_view");
//                    Element newsWriter = doc2.selectFirst(".txt_info");
//                    Element newsDate = doc2.selectFirst(".num_date");
//                    Element imgTag = doc2.selectFirst("p.link_figure img");
//                    Element content = doc2.selectFirst("#mArticle > div.news_view.fs_type1 > div.article_view > section");
//
//                    if (newsName == null || newsWriter == null || newsDate == null || imgTag == null || content == null) {
//                        continue;
//                    }
//
//                    String imgSrc = imgTag.attr("src");
//                    String date[] = newsDate.text().split(" ");
//                    int n_year = Integer.parseInt((date[0]).replace(".", ""));
//                    int n_month = Integer.parseInt((date[1]).replace(".", ""));
//                    int n_date = Integer.parseInt((date[2]).replace(".", ""));
//                    String time[] = (date[3]).split(":");
//                    int n_hour = Integer.parseInt(time[0]);
//                    int n_minute = Integer.parseInt(time[1]);
//
//                    NewsDTO dto = new NewsDTO();
//                    dto.setN_title(newsName.text());
//                    dto.setN_writer(newsWriter.text());
//                    dto.setN_content(content != null ? content.text() : null);
//                    dto.setN_year(n_year);
//                    dto.setN_month(n_month);
//                    dto.setN_date(n_date);
//                    dto.setN_hour(n_hour);
//                    dto.setN_minute(n_minute);
//                    dto.setImg_url(imgSrc);
//                    dto.setNews_url(href);
//
//                    newsService.addNews(dto);
//                    count++;
//                }
//            }
//        } catch(Exception e){
//            e.printStackTrace();
//        }
    }
}
