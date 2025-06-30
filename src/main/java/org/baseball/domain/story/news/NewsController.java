package org.baseball.domain.story.news;

import org.springframework.ui.Model;
import org.baseball.dto.NewsDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/story")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/news")
    public String news(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            Model model){
        Map<String, Object> result = newsService.getNews(page, type, keyword);
        model.addAttribute("list", result.get("list"));
        model.addAttribute("totalCount", result.get("totalCount"));
        model.addAttribute("type", result.get("type"));
        model.addAttribute("keyword", result.get("keyword"));
        model.addAttribute("page", result.get("page"));


        return "story/news";
    }

    @GetMapping("/news/page")
    @ResponseBody
    public Map<String, Object> getNewsPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword){
        return newsService.getNews(page, type, keyword);
    }

    @GetMapping("/news/crawl/sdfsdfsdfsfagfgas")
    public String crawlNews(){
        try{
            Document doc = Jsoup.connect("https://sports.daum.net/baseball").get();
            Elements newsHrefs = doc.select(".list_rank > li");
            for(Element item : newsHrefs){
                Element linkTag = item.selectFirst("a");
                if(linkTag !=null){
                    String href = linkTag.absUrl("href");
                    Document doc2 = Jsoup.connect(href).get();

                    Element newsName = doc2.selectFirst(".tit_view");
                    Element newsWriter = doc2.selectFirst(".txt_info");
                    Element newsDate = doc2.selectFirst(".num_date");
                    Element imgTag = doc2.selectFirst("p.link_figure img");
                    Element content = doc2.selectFirst("#mArticle > div.news_view.fs_type1 > div.article_view > section");

                    if (newsName == null || newsWriter == null || newsDate == null || imgTag == null || content == null) {
                        continue;
                    }

                    String imgSrc = imgTag.attr("src");
                    String date[] = newsDate.text().split(" ");
                    int n_year = Integer.parseInt((date[0]).replace(".", ""));
                    int n_month = Integer.parseInt((date[1]).replace(".", ""));
                    int n_date = Integer.parseInt((date[2]).replace(".", ""));
                    String time[] = (date[3]).split(":");
                    int n_hour = Integer.parseInt(time[0]);
                    int n_minute = Integer.parseInt(time[1]);

                    NewsDTO dto = new NewsDTO();
                    dto.setN_title(newsName.text());
                    dto.setN_writer(newsWriter.text());
                    dto.setN_content(content != null ? content.text() : null);
                    dto.setN_year(n_year);
                    dto.setN_month(n_month);
                    dto.setN_date(n_date);
                    dto.setN_hour(n_hour);
                    dto.setN_minute(n_minute);
                    dto.setImg_url(imgSrc);
                    dto.setNews_url(href);

                    newsService.addNews(dto);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return "redirect:/community/post";
    }
}
